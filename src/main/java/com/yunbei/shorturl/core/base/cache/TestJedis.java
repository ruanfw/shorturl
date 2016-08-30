package com.yunbei.shorturl.core.base.cache;

import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
public class TestJedis {
    // @Autowired
    // private JedisConnectionFactory jedisConnectionFactory;

    public String ping() {
        // Jedis jedis = jedisConnectionFactory.getShardInfo().createResource();

        JedisPool pool = new JedisPool(new JedisPoolConfig(), "115.29.47.18", 6379, 0, "123456");
        Jedis jedis = pool.getResource();

        return jedis.ping();
    }

}
