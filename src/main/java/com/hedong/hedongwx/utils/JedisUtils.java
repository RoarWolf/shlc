package com.hedong.hedongwx.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;


public class JedisUtils {

	private static final Logger logger = LoggerFactory.getLogger(JedisUtils.class);
	private JedisPool jp;
	
	//private static JedisPool jedisPool;
	private static JedisPool pool=null;
	static{
		JedisPoolConfig poolConfig=new JedisPoolConfig();
		poolConfig.setMaxTotal(1000);
		//pool=new JedisPool(poolConfig,"106.14.46.43",6379,0,"redisshzylc");
		pool=new JedisPool(poolConfig,"121.196.187.251",6379,0,"redisshzylc");
	}
	
	//@PostConstruct
	//public void init() {
	//	jedisPool = jp;
	//}

	public static void main(String[] args) {
		Map<String, String> mapParent = new HashMap<>();
		mapParent.put("billver", 1+"");
		mapParent.put("parkingfee", 1+"");
		mapParent.put("timenum", 6+"");
		mapParent.put("timeInfo", 6+"");
		JedisUtils.hmset("billingInfo", mapParent);
		System.err.println(set("admin","123",1222));
	}


	/**
	 * 获取缓存
	 * @param key 键
	 * @return 值
	 */
	public static String get(String key) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				value = jedis.get(key);
				value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
				logger.debug("get {} = {}", key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 获取缓存
	 * @param key 键
	 * @return 值
	 */
	public static String getnum(String key,int num) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(num);
			if (jedis.exists(key)) {
				value = jedis.get(key);
				value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
				logger.debug("get {} = {}", key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 设置缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static String set(String key, String value, int cacheSeconds) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.set(key, value);
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
			logger.debug("set {} = {}", key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 设置缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static String setnum(String key, String value, int cacheSeconds, int num) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(num);
			result = jedis.set(key, value);
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
			logger.debug("set {} = {}", key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	//======================================================================
	public static String setCode(String key, String value, int cacheSeconds) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.set(key, value);
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
			logger.debug("set {} = {}", key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	//=========================================
	public static String getCode(String key) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				value = jedis.get(key);
				value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
				logger.debug("get {} = {}", key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return value;
	}
	//============================================
	public static boolean existsCode(String key) {
		boolean result = false;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.exists(key);
			logger.debug("exists {}", key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	
	
	/**
	 * 获取List缓存
	 * @param key 键
	 * @return 值
	 */
	public static List<String> lrange(String key) {
		List<String> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				value = jedis.lrange(key, 0, -1);
				logger.debug("getList {} = {}", key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 向List缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static long lpush(String key, String... value) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.lpush(key, value);
			logger.debug("listAdd {} = {}", key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 向List缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static long rpush(String key, String... value) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.rpush(key, value);
			logger.debug("listAdd {} = {}", key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 获取缓存
	 * @param key 键
	 * @return 值
	 */
	public static Set<String> smembers(String key) {
		Set<String> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				value = jedis.smembers(key);
				logger.debug("getSet {} = {}", key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 向Set缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static long sadd(String key, String... value) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.sadd(key, value);
			logger.debug("setSetAdd {} = {}", key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	public static long zadd(String key, String member, Double score) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.zadd(key, score, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	public static long zrem(String key, String... members) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.zrem(key, members);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	public static Set<String> zrevrange(String key) {
		Set<String> result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.zrevrange(key, 0, -1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 获取Map缓存
	 * @param key 键
	 * @return 值
	 */
	public static Map<String, String> hgetAll(String key) {
		Map<String, String> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				value = jedis.hgetAll(key);
				logger.debug("getMap {} = {}", key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return value;
	}



	
	public static String hget(String key, String field) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				value = jedis.hget(key, field);
				logger.debug("getMap {} = {}", key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return value;
	}
	
	public static Long hdel(String key, String... fields) {
		Long value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				value = jedis.hdel(key, fields);
				logger.debug("getMap {} = {}", key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 设置Map缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static String hmset(String key, Map<String, String> value, int cacheSeconds) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				jedis.del(key);
			}
			result = jedis.hmset(key, value);
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
			logger.debug("setMap {} = {}", key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 向Map缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static String hmset(String key, Map<String, String> value) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.hmset(key, value);
			logger.debug("mapPut {} = {}", key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	
	public static Long hincrBy(String key, String field, long value) {
		Long result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.hincrBy(key, field, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 判断Map缓存中的Key是否存在
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static boolean hexists(String key, String mapKey) {
		boolean result = false;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.hexists(key, mapKey);
			logger.debug("mapExists {}  {}", key, mapKey);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 删除缓存
	 * @param key 键
	 * @return
	 */
	public static long del(String key) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)){
				result = jedis.del(key);
				logger.debug("del {}", key);
			}else{
				logger.debug("del {} not exists", key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	/**
	 * 更新缓存端口
	 * @param key 键
	 * @return 返回0 更新成功
	 */
	public static long upDateCache(String code,String field,String portValue) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(code)){
				jedis.hset(code, field, portValue);
				logger.debug("del {}", code);
			}else{
				logger.debug("del {} not exists", code);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 缓存是否存在
	 * @param key 键
	 * @return
	 */
	public static boolean exists(String key) {
		boolean result = false;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.exists(key);
			logger.debug("exists {}", key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 获取资源
	 * @return
	 * @throws JedisException
	 */
	public static Jedis getResource() throws JedisException {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			
//			logger.debug("getResource.", jedis);
		} catch (JedisException e) {
			e.printStackTrace();
			returnBrokenResource(jedis);
			throw e;
		}
		return jedis;
	}
	//添加緩存
	public static void addEquipmentCache(String code,Map<String,String> portMap){
		Jedis jedis = null;
		if(code !=null && !"".equals(code)){
			try {
				jedis = getResource();
				if (jedis.exists(code) && portMap !=null && !portMap.isEmpty()){
					for (Entry<String, String> entry : portMap.entrySet() ) {
						String keyPort=entry.getKey();
						String value=entry.getValue();
						System.out.println(keyPort + "===" + value);
						//判斷緩存中的端口信息是否存在
						if(jedis.hexists(code, keyPort)){
							//存在，新值將老值覆蓋
							jedis.hset(code, keyPort, value);
							logger.debug("del {} not exists", code+"設備緩存"+keyPort+"端口更新成功");
						}else{
							//緩存中的端口信息不存在就添加
							jedis.hset(code, keyPort, value);
							logger.debug("del {} not exists", code+"設備緩存"+keyPort+"端口添加成功");
						}
					}
				}else{
					//添加設備緩存
					if(portMap !=null && !portMap.isEmpty()){
						jedis.hmset(code, portMap);
						logger.debug("del {} not exists", code+"設備緩存添加成功");
					}else{
						logger.debug("del {} not exists", code+"設備緩存添加失敗");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				returnResource(jedis);
			}
		}else{
			logger.debug("del {} not exists", code+"設備號未知");
		}
	}
	/**
	 * 归还资源
	 * @param jedis
	 * @param isBroken
	 */
	public static void returnBrokenResource(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
//			pool.returnBrokenResource(jedis);
		} 
	}
	
	/**
	 * 释放资源
	 * @param jedis
	 * @param isBroken
	 */
	public static void returnResource(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
//			pool.returnResource(jedis);
		} 
	}
	
}