package com.jwj.im.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import com.google.common.collect.Lists;


public class BaseDao {

	/**
	 * 分页查询
	 * 
	 * @param jdbc
	 * @param sql
	 * @param page
	 * @param params
	 */
	protected void pageForMap(JdbcTemplate jdbc, String sql, Pagetool<Map<String, Object>> page, Object... params) {
		String bsql = sqlBuild(sql, page);
		List<Map<String, Object>> list = Lists.newArrayList();
		if (params != null && params.length > 0) {
			list = jdbc.queryForList(bsql, params);
		} else {
			list = jdbc.queryForList(bsql);
		}
		int total = total(jdbc, sql, params);

		page.setTotalRow(total);
		page.setList(list);
	}

	/**
	 * 分页查询
	 * 
	 * @param jdbc
	 * @param sql
	 * @param page
	 * @param params
	 */
	protected List<Map<String, Object>> pageForMap(JdbcTemplate jdbc, String sql, Object... params) {
		List<Map<String, Object>> list = Lists.newArrayList();
		if (params != null && params.length > 0) {
			list = jdbc.queryForList(sql, params);
		} else {
			list = jdbc.queryForList(sql);
		}
		return list;
	}

	/**
	 * 分页查询
	 * 
	 * @param jdbc
	 * @param sql
	 * @param page
	 * @param clazz
	 * @param params
	 */
	protected <T> void pageForBean(JdbcTemplate jdbc, String sql, Pagetool<T> page, Class<T> clazz, Object... params) {
		String bsql = sqlBuild(sql, page);
		List<T> list = queryForListBean(jdbc, bsql, clazz, params);
		int total = total(jdbc, sql, params);

		page.setTotalRow(total);
		page.setList(list);
	}

	protected <T> List<T> pageForBean(JdbcTemplate jdbc, String sql, Class<T> clazz, Object... params) {
		return queryForListBean(jdbc, sql, clazz, params);
	}

	public <T> List<T> queryForListBean(JdbcTemplate jdbc, String sql, Class<T> clazz, Object... params) {
		RowMapper<T> rowMapper = new BeanPropertyRowMapper<>(clazz);

		List<T> list = Lists.newArrayList();
		if (params != null && params.length > 0) {
			list = jdbc.query(sql, rowMapper, params);

		} else {
			list = jdbc.query(sql, rowMapper);
		}

		return list;
	}

	private int total(JdbcTemplate jdbc, String sql, Object... params) {

		StringBuffer sb = new StringBuffer("select count(*) from ( ");
		sb.append(sql);
		sb.append(" ) t");

		Integer count = jdbc.queryForObject(sb.toString(), Integer.class, params);

		return count;
	}

	private <T> String sqlBuild(String sql, Pagetool<T> page) {
		int start = page.getPageNumber();// 第几页开始
		int size = page.getPageSize();// 每次查询多少条
		if (start > 0) {
			start = start * size;
		}
		StringBuffer sb = new StringBuffer("select * from (");
		sb.append(sql);
		sb.append(" ) t limit ");
		// 分页优化 超过5W使用 偏移
		if (start > 50000) {
			sb.append(size);
			sb.append(" offset ");
			sb.append(start);
		} else {
			sb.append(start);
			sb.append(" , ");
			sb.append(size);
		}

		return sb.toString();
	}

	private String sqlBuild(String sql, int limit) {
		StringBuffer sb = new StringBuffer("select * from (");
		sb.append(sql);
		sb.append(" ) t limit ");
		sb.append(limit);

		return sb.toString();
	}

	protected Map<String, Object> callProcedure(JdbcTemplate jdbc, String procedureName, SqlParameter[] sqlParameters,
			Map<String, Object> inParamMap, List<String> outParamList) {
		Map<String, Object> outDataMap = new HashMap<>();
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbc).withProcedureName(procedureName)
				.declareParameters(sqlParameters);
		Map<String, Object> out = simpleJdbcCall.execute(inParamMap);
		for (String key : outParamList) {
			outDataMap.put(key, out.get(key));
		}
		return outDataMap;
	}

}
