package com.yunbei.shorturl.core.cache;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.yunbei.shorturl.core.base.utils.ProtoStuffSerializerUtil;
import com.yunbei.shorturl.core.cache.parse.EveryParser;

/**
 * redis
 * 
 * @author Administrator
 *
 */
@Component
public class RedisCache {

	private static final Logger LOG = LoggerFactory.getLogger(RedisCache.class);

	public static final int CAHCETIME = 60;// 默认缓存时间60s

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	public <T> boolean setNx(String key, T obj) {
		final byte[] bkey = key.getBytes();
		final byte[] bvalue = ProtoStuffSerializerUtil.serialize(obj);

		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.setNX(bkey, bvalue);
			}
		});
		return result;
	}

	public <T> void setEx(String key, T obj, final long expireTime) {
		final byte[] bkey = key.getBytes();
		final byte[] bvalue = ProtoStuffSerializerUtil.serialize(obj);
		redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				connection.setEx(bkey, expireTime, bvalue);
				return true;
			}
		});
	}

	public <T> boolean setNxList(String key, List<T> objList) {
		final byte[] bkey = key.getBytes();
		final byte[] bvalue = ProtoStuffSerializerUtil.serializeList(objList);

		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.setNX(bkey, bvalue);
			}
		});
		return result;
	}

	public <T> boolean setExList(String key, List<T> objList, final long expireTime) {
		final byte[] bkey = key.getBytes();
		final byte[] bvalue = ProtoStuffSerializerUtil.serializeList(objList);
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				connection.setEx(bkey, expireTime, bvalue);
				return true;
			}
		});
		return result;
	}

	public <T> T get(final String key, Class<T> targetClass) {
		byte[] result = redisTemplate.execute(new RedisCallback<byte[]>() {
			@Override
			public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.get(key.getBytes());
			}
		});
		if (result == null) {
			return null;
		}
		return ProtoStuffSerializerUtil.deserialize(result, targetClass);
	}

	public <T> List<T> getList(final String key, Class<T> targetClass) {
		byte[] result = redisTemplate.execute(new RedisCallback<byte[]>() {
			@Override
			public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.get(key.getBytes());
			}
		});
		if (result == null) {
			return null;
		}
		return ProtoStuffSerializerUtil.deserializeList(result, targetClass);
	}

	/**
	 * 精确删除key
	 *
	 * @param key
	 */
	public void del(String key) {
		redisTemplate.delete(key);
	}

	/**
	 * 模糊删除key
	 *
	 * @param pattern
	 */
	public void delWithPattern(String pattern) {
		Set<String> keys = redisTemplate.keys(pattern);
		redisTemplate.delete(keys);
	}

	public <T> Long lpushObj(String key, T obj) {
		final byte[] bvalue = ProtoStuffSerializerUtil.serialize(obj);
		final byte[] bkey = key.getBytes();

		Long result = redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.lPush(bkey, bvalue);
			}
		});
		if (result == null) {
			return null;
		}
		return result;
	}

	public <T> T rpopObj(final String key, Class<T> targetClass) {

		final byte[] bkey = key.getBytes();

		byte[] result = redisTemplate.execute(new RedisCallback<byte[]>() {
			@Override
			public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.rPop(bkey);
			}
		});
		if (result == null) {
			return null;
		}
		return ProtoStuffSerializerUtil.deserialize(result, targetClass);
	}

	public <T> T brpopOnlyObj(final int timeout, final String key, Class<T> targetClass) {

		final byte[] bkey = key.getBytes();

		List<byte[]> result = redisTemplate.execute(new RedisCallback<List<byte[]>>() {
			@Override
			public List<byte[]> doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.bRPop(timeout, bkey);
			}
		});
		if (result == null) {
			return null;
		}
		return ProtoStuffSerializerUtil.deserialize(result.get(1), targetClass);
	}

	/**
	 * 检查连接
	 * 
	 * @return
	 */
	public String ping() {
		return redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.ping();
			}
		});
	}

	public <T> String genKey(Class<T> clazz, String... params) {

		try {
			return EveryParser.genKey(clazz, params);
		} catch (Exception e) {
			LOG.warn(e.getMessage(), e);
		}
		return StringUtils.EMPTY;
	}

	public <T> String genKey(Class<T> clazz) {

		try {
			return EveryParser.genKey(clazz);
		} catch (Exception e) {
			LOG.warn(e.getMessage(), e);
		}
		return StringUtils.EMPTY;
	}

	public String genKey(Object obj) {

		try {
			return EveryParser.genKey(obj);
		} catch (Exception e) {
			LOG.warn(e.getMessage(), e);
		}
		return StringUtils.EMPTY;
	}
}
