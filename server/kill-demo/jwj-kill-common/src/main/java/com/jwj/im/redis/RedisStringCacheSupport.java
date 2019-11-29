package com.jwj.im.redis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands.Tuple;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;


/**
 * 
 * redis缓存实现
 * 
 */
@Component
public class RedisStringCacheSupport {

	private static final Logger logger = LoggerFactory.getLogger(RedisStringCacheSupport.class);
	// -1 - never expire
	//	private int expire = -1;

	//    private int dbIndex = RedisDbIndexTypeE.用户.value;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;


	private byte[] rawString(String value) {
		return redisTemplate.getStringSerializer().serialize(value);
	}

	private String doneString(byte[] bs) {
		return redisTemplate.getStringSerializer().deserialize(bs);
	}

	/**
	 * 用于判断redis是否在线
	 * reids 连接命令 ping
	 * @return
	 */
	public boolean isConnection(){  	
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				String ping=connection.ping();

				return "PONG".equals(ping);
			}
		});
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void put(final String key, final String value) {
		put(key, value, null);
	}


	/**
	 * 设置指定 key 的值
	 * reids 字符串 set命令
	 * @param key
	 * @param value
	 * @param expire 单位/秒
	 */
	public void put( final String key, final String value, final Long expire) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] kb = key.getBytes();
				/* connection.select(dbIndex);  */
				//				connection.openPipeline();

				connection.set(kb, rawString(value));
				if (expire != null){
					connection.expire(kb, expire);
				}
				return null;
			}
		});
	}
	
	/**
	 * 对存储在指定key的数值执行原子的加1操作
	 * @param key
	 */
	public String incr( final String key) {
		return redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] kb = key.getBytes();
				/* connection.select(dbIndex);  */
				//				connection.openPipeline();
				return connection.incr(kb)+"";
			}
		});
	}

	/**
	 * 对存储在指定key的数值执行原子的减1操作
	 * @param key
	 */
	public String decr( final String key) {
		return redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] kb = key.getBytes();
				/* connection.select(dbIndex);  */
				//				connection.openPipeline();
				return connection.decr(kb)+"";
			}
		});
	}
	

	/**
	 * redis追加key原来的值
	 * reids 字符串 append命令
	 * @param key
	 * @param value
	 */
	public void append( final String key, final String value){
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] kb = key.getBytes();
				/* connection.select(dbIndex);  */
				//				connection.openPipeline();
				connection.append(kb, rawString(value));
				return null;
			}

		});
	}

	/**
	 * redis 获取指定 key 的值。
	 * reids 字符串 get命令
	 * @param key
	 * @return
	 */
	public String getCached(final String key) {
		return redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				byte[] bs = connection.get(key.getBytes());
				if (bs == null) {
					return null;
				}
				return doneString(bs);
			}
		});
	}

	/**
	 * 更新缓存
	 * reids 字符串 set命令
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public void updateCached( final String key, final String value) throws Exception {
		updateCached(key, value, null);
	}

	public void updateCached(final String key, final String value, final Long expireSec) throws Exception {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public String doInRedis(final RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				connection.set(key.getBytes(), rawString(value));
				if (expireSec != null) {
					connection.expire(key.getBytes(), expireSec);
				} else {
					connection.expire(key.getBytes(),-1);
				}
				return null;
			}
		});

	}

	/**
	 * 删除缓存
	 * reids 字符串 del命令
	 * @param keys
	 * @throws Exception
	 */
	public void deleteCached(final String... keys) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Long doInRedis(RedisConnection connection) {
				byte[][] byteKeys = new byte[keys.length][];
				for (int i = 0; i < keys.length; i++) {
					byteKeys[i] = keys[i].getBytes();
				}
				/* connection.select(dbIndex);  */
				return connection.del(byteKeys);
			}
		});
	}


	/**
	 * redis是否存在key值
	 * @param key
	 * @return
	 */
	public Boolean isKey(final String key) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				return connection.exists(key.getBytes());
			}
		});
	}

	/**
	 * 查找所有符合给定模式(pattern)的 key。
	 * Redis 键(key) Keys 命令
	 * @param pattern
	 * @return
	 */
//	public Set<String> getKeys( final String pattern) throws Exception {
//		return redisTemplate.execute(new RedisCallback<Set<String>>() {
//			public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
//				/* connection.select(dbIndex);  */
//				Set<byte[]> setByte = connection.keys(pattern.getBytes());
//				if (setByte == null || setByte.size() < 1) {
//					return null;
//				}
//				Set<String> set = new HashSet<String>();
//				for (byte[] key : setByte) {
//					byte[] bs = connection.get(key);
//					set.add(doneString(bs));
//				}
//
//				return set;
//
//			}
//		});
//	}

	/**
	 * 查找所有符合给定模式(pattern)的 key。
	 * Redis 键(key) Keys 命令
	 * @param pattern
	 * @return
	 */
//	public String[] keys(final String pattern) {
//		return redisTemplate.execute(new RedisCallback<String[]>() {
//			public String[] doInRedis(RedisConnection connection) throws DataAccessException {
//				/* connection.select(dbIndex);  */
//				Set<byte[]> setByte = connection.keys(pattern.getBytes());
//
//				if (setByte == null || setByte.size() < 1) {
//					return null;
//				}
//				String[] keys = new String[setByte.size()];
//				int index = 0;
//				for (byte[] key : setByte) {
//					keys[index++] = new String(key);
//				}
//				return keys;
//			}
//		});
//	}

	private List<byte[]> _mGet(final String... keys) {
		return redisTemplate.execute(new RedisCallback<List<byte[]>>() {
			@Override
			public List<byte[]> doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				if (keys == null || keys.length == 0) {
					return null;
				}
				byte[][] byteKeys = new byte[keys.length][];
				for (int i = 0; i < keys.length; i++) {
					byteKeys[i] = keys[i].toString().getBytes();
				}
				List<byte[]> result = connection.mGet(byteKeys);
				if (result == null || result.size() == 0) {
					return null;
				}
				return result;
			}
		});
	}

	/**
	 * 获取所有(一个或多个)给定 key 的值。
	 * Redis 字符串(String) Mget 命令
	 * @param keys
	 * @return
	 */
	public List<String> mGet(final String... keys) {
		List<byte[]> byteList = _mGet( keys);
		if (byteList == null || byteList.size() == 0) {
			return null;
		}

		List<String> result = new ArrayList<String>();
		for (byte[] bs : byteList) {
			result.add(doneString(bs));
		}
		return result;
	}


//	private void _mSet( final Map<byte[], byte[]> tuple) {
//		redisTemplate.execute(new RedisCallback<Object>() {
//			public Object doInRedis(RedisConnection connection) throws DataAccessException {
//				/* connection.select(dbIndex);  */
//				connection.mSet(tuple);
//				return null;
//			}
//		});
//	}

	/**
	 * 同时设置一个或多个 key-value 对。
	 * Redis 字符串(String) Mset 命令
	 * @param tuple
	 */
//	public void mSet( final Map<String, String> tuple) {
//		Map<byte[], byte[]> vTuple = new HashMap<byte[], byte[]>();
//		for (String key : tuple.keySet()) {
//			vTuple.put(key.getBytes(), rawString(tuple.get(key)));
//		}
//		_mSet(dbIndex, vTuple);
//	}



	/**
	 * 获取所有哈希表中的字段
	 * Redis 哈希(Hash) Hkeys 命令
	 * @param key
	 * @return
	 */
	public Set<String> getHashKeys(final String key) {
		return redisTemplate.execute(new RedisCallback<Set<String>>() {
			@Override
			public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				Set<byte[]> hKeys = connection.hKeys(key.getBytes());
				if (hKeys == null || hKeys.size() == 0) {
					return null;
				}
				Set<String> set = new HashSet<String>();
				for (byte[] bs : hKeys) {
					set.add(new String(bs));
				}
				return set;
			}
		});

	}


	/**
	 * 将哈希表 key 中的字段 field 的值设为 value 。
	 * Redis 哈希(Hash) Hset 命令
	 * @param key
	 * @param mapkey
	 * @param value
	 * @return
	 */
	public Boolean putHashCached( final String key, final String mapkey, final String value) {

		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				Boolean hSet = connection.hSet(key.getBytes(), mapkey.getBytes(), rawString(value));
				return hSet;
			}
		});
	}

	/**
	 * 获取存储在哈希表中指定字段的值
	 * Redis 哈希(Hash) Hget 命令
	 * @param key
	 * @param mapkey
	 * @return
	 */
	public String getHashCached( final String key, final String mapkey) {
		return redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				byte[] hGet = connection.hGet(key.getBytes(), mapkey.getBytes());

				return doneString(hGet);

			}
		});
	}

	/**
	 * 删除一个或多个哈希表字段
	 * Redis 哈希(Hash) Hdel 命令
	 * @param key
	 * @param mapkey
	 * @return
	 */
	public Long deleteHashCached( final String key, final String mapkey) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				Long hDel = connection.hDel(key.getBytes(), mapkey.getBytes());
				return hDel;
			}
		});
	}

	/**
	 * 获取哈希表中字段的数量
	 * Redis 哈希(Hash) Hlen 命令
	 * @param key
	 * @return
	 */
	public Long getHashSize( final String key)  {
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				Long len = connection.hLen(key.getBytes());
				return len;
			}
		});
	}

	/**
	 * 获取哈希表中所有值
	 * Redis 哈希(Hash) Hvals 命令
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public List<String> getHashValues( final String key) throws Exception {
		return redisTemplate.execute(new RedisCallback<List<String>>() {
			@Override
			public List<String> doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				List<byte[]> hVals = connection.hVals(key.getBytes());

				if (hVals == null || hVals.size() == 0) {
					return null;
				}
				List<String> list = new ArrayList<String>();

				for (byte[] bs : hVals) {
					list.add(doneString(bs));
				}
				return list;

			}
		});
	}

	/**
	 * 获取在哈希表中指定 key 的所有字段和值
	 * Redis 哈希(Hash) Hgetall 命令
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> hGetAll(final String key) throws Exception {
		return redisTemplate.execute(new RedisCallback<Map<String,String>>() {
			@Override
			public Map<String,String> doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				Map<byte[], byte[]> hashValues=connection.hGetAll(key.getBytes());
				Map<String,String> maps=new HashMap<>();
				for (byte[] field : hashValues.keySet()) {
					maps.put(new String(field), doneString(hashValues.get(field)));
				}
				return maps;
			}
		});
	}

	/**
	 * 查看哈希表 key 中，指定的字段是否存在。
	 *  Redis 哈希(Hash) Hexists 命令
	 * @param key
	 * @param mapkey
	 * @return
	 */
	public boolean hExists(final String key,final String mapkey){
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) {	
				/* connection.select(dbIndex);  */
				return connection.hExists(key.getBytes(), mapkey.getBytes());
			}
		});
	}

	/**
	 * 获取存储在哈希表中指定字段的值
	 *  Redis 哈希(Hash) Hget 命令
	 * @param key
	 * @param fields
	 * @return
	 */
	public List<String> hMGet( final String key, final Object... fields) {	
		return redisTemplate.execute(new RedisCallback<List<String>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<String> doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				List<String> result = new ArrayList<String>();
				if (fields == null || fields.length == 0) {
					return null;
				}
				byte[][] byteFields = new byte[fields.length][];
				for (int i = 0; i < fields.length; i++) {
					byteFields[i] = fields[i].toString().getBytes();
				}
				List<byte[]> hmaps = connection.hMGet(key.getBytes(), byteFields);
				if (hmaps == null || hmaps.size() == 0) {
					return Collections.EMPTY_LIST;
				}
				for (byte[] bs : hmaps) {
					result.add(doneString(bs));
				}
				return result;
			}
		});
	}

	/**
	 * 同时将多个 field-value (域-值)对设置到哈希表 key 中。
	 * Redis 哈希(Hash) Hmset 命令
	 * @param key
	 * @param hashValues
	 */
	public void hMSet(final String key, final Map<String, String> hashValues) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				Map<byte[], byte[]> hashes = new HashMap<byte[], byte[]>();
				for (String field : hashValues.keySet()) {
					hashes.put(field.getBytes(), rawString(hashValues.get(field)));
				}
				connection.hMSet(key.getBytes(), hashes);
				return null;
			}
		});
	}

	/**
	 * 向有序集合添加一个或多个成员，或者更新已存在成员的分数
	 * Redis 有序集合(sorted set) Zadd 命令
	 * @param key
	 * @param tuples
	 */
	public void zAdd(final String key, final LinkedHashSet<String> tuples) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				Set<Tuple> vTuples = new LinkedHashSet<Tuple>();
				double score = 0;
				for (String value : tuples) {
					vTuples.add(new SimpleTuple(rawString(value), score++));
				}
				connection.zAdd(key.getBytes(), vTuples);

				return null;
			}
		});
	}

	/**
	 * 通过索引区间返回有序集合成指定区间内的成员
	 * Redis 有序集合(sorted set) Zrange 命令
	 * @param key
	 * @param begin
	 * @param end
	 * @return
	 */
	public Set<String> zRange( final String key, final long begin, final long end) {
		return redisTemplate.execute(new RedisCallback<Set<String>>() {
			@Override
			public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				Set<byte[]> setBytes = connection.zRange(key.getBytes(), begin, end);
				if (setBytes == null || setBytes.size() == 0) {
					return null;
				}

				Set<String> result = new LinkedHashSet<String>();
				for (byte[] bs : setBytes) {
					result.add(doneString(bs));
				}
				return result;
			}
		});
	}

	/**
	 * 向有序集合添加一个或多个成员，或者更新已存在成员的分数
	 * Redis 有序集合(sorted set) Zadd 命令
	 * @param key
	 * @param score
	 * @param value
	 */
	public void zAdd( final String key,int score,final String value){
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				connection.zAdd(key.getBytes(), score, rawString(value));
				return null;
			}
		});
	}

	/**
	 * 通过分数返回有序集合指定区间内的成员
	 * Redis 有序集合(sorted set) Zrangebyscore 命令
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public Set<String> zRangeScore( final String key,int min,int max){
		return redisTemplate.execute(new RedisCallback<Set<String>>() {
			@Override
			public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				Set<byte[]> setBytes=connection.zRangeByScore(key.getBytes(), min, max);

				Set<String> result = new LinkedHashSet<String>();
				for (byte[] bs : setBytes) {
					result.add(doneString(bs));
				}
				return result;
			}
		});
	}


	/**
	 * 向集合添加一个或多个成员
	 * Redis 集合(Set) Sadd 命令
	 * @param key
	 * @param value
	 */
	public void sAdd( final String key,final String...value){
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				if(value==null||value.length==0){
					return null;
				}
				byte[][] byteValues = new byte[value.length][];

				for (int i = 0; i < value.length; i++) {

					byteValues[i] = rawString(value[i]);
				}
				connection.sAdd(key.getBytes(), byteValues);

				return null;
			}
		});
	}

	/**
	 * 返回集合中的所有成员
	 * Redis 集合(Set) Smembers 命令
	 * @param key
	 * @return
	 */
	public Set<String> sMembers(final String key){
		return redisTemplate.execute(new RedisCallback<Set<String>>() {
			@Override
			public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				Set<byte[]> setBytes=connection.sMembers(key.getBytes());

				Set<String> result = new LinkedHashSet<String>();
				for (byte[] bs : setBytes) {
					result.add(doneString(bs));
				}
				return result;
			}
		});
	}

	/**
	 * 获取集合的成员数
	 * Redis 集合(Set) sCard命令
	 * @param key
	 * @return
	 */
	public long sCard(final String key){
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				return connection.sCard(key.getBytes());
			}
		});
	}

	/**
	 * 判断 member 元素是否是集合 key 的成员
	 * Redis 集合(Set)  Sismember命令 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean sIsMember( final String key,final String value){
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				return connection.sIsMember(key.getBytes(), rawString(value));
			}
		});
	}

	/**
	 * 移除并返回集合中的一个随机元素。
	 * Redis 列表(Set)  spop 命令
	 * @param key
	 * @return
	 */
	public Object sPop( final String key){
		return redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				byte[] value=connection.sPop(key.getBytes());
				return new String(value);
			}
		});
	}

	/**
	 * 移除并返回集合中的一个随机元素。
	 * Redis 列表(Set)  spop 命令
	 * @param key
	 * @return
	 */
	public Long sRem( final String key,final String...value){
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				byte[][] byteValues = new byte[value.length][];

				for (int i = 0; i < value.length; i++) {
					byteValues[i] = value[i].toString().getBytes();
				}
				return connection.sRem(key.getBytes(),byteValues);
			}
		});
	}

	/**
	 * 在列表中添加一个或多个值
	 * Redis 列表(List)  Rpush 命令
	 * @param key
	 * @param value
	 * @return
	 */
	public Long rPush ( final String key,final String...value){
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				byte[][] byteValues = new byte[value.length][];

				for (int i = 0; i < value.length; i++) {
					byteValues[i] = rawString(value[i]);
				}
				return connection.rPush(key.getBytes(), byteValues);
			}
		});
	}

	/**
	 * 获取列表指定范围内的元素
	 * Redis 列表(List) Lrange 命令 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Object> lRange( final String key,long start,long end){
		return redisTemplate.execute(new RedisCallback<List<Object>>() {
			@Override
			public List<Object> doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				List<byte[]> values=connection.lRange(key.getBytes(), start, end);

				List<Object> result=Lists.newArrayList();
				for (byte[] bs : values) {
					result.add(doneString(bs));
				}
				return result;
			}
		});
	}

	public List<Object> lRangeMessage( final String key,long start,long end){
		return redisTemplate.execute(new RedisCallback<List<Object>>() {
			@Override
			public List<Object> doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				List<byte[]> values=connection.lRange(key.getBytes(), start, end);

				List<Object> result=Lists.newArrayList();
				for (byte[] bs : values) {
					result.add(new String(bs));
				}
				return result;
			}
		});
	}

	/**
	 * 获取列表长度
	 * Redis 列表(List)  Llen 命令 
	 * @param key
	 * @return
	 */
	public Long lLen ( final String key){
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */

				return connection.lLen(key.getBytes());
			}
		});
	}

	/**
	 * 让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
	 * Redis 列表(List) Ltrim 命令 - 对一个列表进行修剪(trim)
	 * @param key
	 * @param start 
	 * @param stop
	 */
	public void lTrim( final String key,long start,long stop){
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				connection.lTrim(key.getBytes(),start,stop);
				return null;
			}
		});
	}
	
	
	/**
	 * 
	 * @param dbIndex
	 *            选择redis 第几号数据库 集群不支持
	 * @param key
	 *            集合名称
	 * @param count
	 *            count > 0 : 从表头开始向表尾搜索，移除与 VALUE 相等的元素，数量为 COUNT 。 count = 0 :
	 *            移除表中所有与 VALUE 相等的值。
	 * @param value
	 *            count < 0 : 从表尾开始向表头搜索，移除与 VALUE 相等的元素，数量为 COUNT 的绝对值。
	 */
	public void lRem(final int dbIndex, final String key, long count, final String value) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex); */
				connection.lRem(key.getBytes(), count, value.getBytes());
				return null;
			}
		});
	}



	/**
	 * redis保存
	 * reids 服务器 bgsave
	 */
	public void saveDb() {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				connection.bgSave();
				return null;
			}

		});
	}

	//	public void setDbIndex(RedisDbIndexTypeE index) {
	//		this.dbIndex = index.value;
	//	}

	public void setExpire(final String key, final long seconds) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				return connection.expire(key.getBytes(), seconds);
			}
		});
	}


	/**
	 * 获取redis数据库大小
	 *  reids 服务器 Dbsize命令
	 * @return
	 * @throws Exception
	 */
	public Long getDBSize() throws Exception {
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				Long len = connection.dbSize();
				return len;
			}
		});
	}

	/**
	 * 清空redis数据库缓存
	 * reids 服务器 Flushdb命令
	 * @throws Exception
	 */
	public void clearDB() throws Exception {
		redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				/* connection.select(dbIndex);  */
				connection.flushDb();
				return null;
			}
		});
	}


	/**
	 * 订阅消息
	 * @param channel
	 * @param messageListener
	 */
	public void subscribe(final String channel, MessageListener messageListener) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				connection.subscribe(messageListener,channel.getBytes());
				return null;
			}
		});
	}

	/**
	 * 将信息发送到指定的频道。
	 * Redis 发布订阅  Publish 命令
	 * @param channel
	 * @param message
	 */
	public void sendMessage(String channel, String message) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) {
				Long l=connection.publish(channel.getBytes(), message.getBytes());
				return null;
			}
		});

	}



}
