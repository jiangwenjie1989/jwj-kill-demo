package com.jwj.im.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;

/**
 * 分页封装
 */
public class Pagetool<T> implements Serializable {

	private static final long serialVersionUID = -396042381042604616L;
	
	private static final int default_pageNumber=0;
	
	private static final int default_pageSize=10;
	
	private static final int default_maxSize=20;
	/**
	 * 分页查询参数
	 */
	private Map<String, Object> queryParam;// 查询条件
	
	@ApiModelProperty(value="排序条件 ")
	private String orderColunm;// 排序条件
	
	@ApiModelProperty(value="排序方式")
	private String orderMode;// 排序方式
	
	@ApiModelProperty(value="第几页   从0 开始计算 ")
	private int pageNumber = default_pageNumber;// 第几页
	
	@ApiModelProperty(value="每页显示几多")
	private int pageSize = default_pageSize;// 每页显示几多

	/**
	 * 分页结果住数据
	 */
	private List<T> list; // 当前页数据
	
	@ApiModelProperty(value="总页数")
	private int totalPage; // 总页数
	
	@ApiModelProperty(value="总记录数")
	private int totalRow; // 总行数

	/**
	 * 分页返回扩展数据
	 */
	private Object extData; // 返回扩展数据
	
	/**
	 * 分页显示辅助属性
	 */
	@ApiModelProperty(value="当前页数    从1 开始计算 ")
	private int currentPageCount;// 当前页记录数量
	
	@ApiModelProperty(value="是否第一页")
	private boolean isFirst;// 是否第一页
	
	@ApiModelProperty(value="是否最后一页")
	private boolean isLast;// 是否最后一页

	/**
	 * 分页计算
	 */
	public void compute() {
		if(totalRow == 0){
			getTotalPage();
		}
		
		this.currentPageCount = (null != this.list ? this.list.size() : 0);// 当前页记录数

		if (pageNumber == 1) {
			this.isFirst = true;
		} else {
			this.isFirst = false;
		}

		if (pageNumber == totalPage) {
			this.isLast = true;
		} else {
			this.isLast = false;
		}
	}

	/**
	 * 计算总页数
	 * @return
	 */
	public int getTotalPage() {
		if ((this.totalRow % this.pageSize) == 0) {
			this.totalPage = this.totalRow / this.pageSize;// 计算多少页
		} else {
			this.totalPage = this.totalRow / this.pageSize + 1;// 计算多少页
		}
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalRow() {
		return totalRow;
	}

	public void setTotalRow(int totalRow) {
		this.totalRow = totalRow;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public Map<String, Object> getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(Map<String, Object> queryParam) {
		this.queryParam = queryParam;
	}

	public String getOrderColunm() {
		return orderColunm;
	}

	public void setOrderColunm(String orderColunm) {
		this.orderColunm = orderColunm;
	}

	public String getOrderMode() {
		return orderMode;
	}

	public void setOrderMode(String orderMode) {
		this.orderMode = orderMode;
	}

	/**
	 * 需要查询显示第几页
	 * @return
	 */
	public int getPageNumber() {
		if (pageNumber <= 0) {
			pageNumber = default_pageNumber;
		}
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	/**
	 * 每页显示多少条数据
	 * @return
	 */
	public int getPageSize() {
		if (pageSize < 0) {
			pageSize = default_pageSize;
		}
		if (pageSize > default_maxSize) {
			pageSize = default_pageSize;
		}
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPageCount() {
		return currentPageCount;
	}

	public void setCurrentPageCount(int currentPageCount) {
		this.currentPageCount = currentPageCount;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public boolean isLast() {
		return isLast;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	public Object getExtData() {
		return extData;
	}

	public void setExtData(Object extData) {
		this.extData = extData;
	}

}
