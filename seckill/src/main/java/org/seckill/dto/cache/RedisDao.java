package main.java.org.seckill.dto.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import main.java.org.seckill.entity.Seckill;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private JedisPool jedisPool;

	public RedisDao(String ip, int port) {
		jedisPool = new JedisPool(ip, port);
	}

	private RuntimeSchema<Seckill> schema = RuntimeSchema
			.createFrom(Seckill.class);

	public Seckill getSeckill(long seckillId) {
		// redis操作逻辑
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:" + seckillId;
				// 并没有实现内部序列化操作
				// 使用protostuff
				byte[] bytes = jedis.get(key.getBytes());
				// 是否获取
				if (bytes != null) {
					Seckill seckill = schema.newMessage();
					ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
					// seckill是反序列出来的对象
					return seckill;
				}
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public String putSeckill(Seckill seckill) {
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:" + seckill.getSeckillId();
				byte[] bytes = ProtostuffIOUtil
						.toByteArray(seckill, schema, LinkedBuffer
								.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
				int seconds = 60*60;
				return jedis.setex(key.getBytes(), seconds, bytes);
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}
}
