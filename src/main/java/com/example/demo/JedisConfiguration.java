package com.example.demo;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

public class JedisConfiguration {
	private static JedisPool jedisPool;

	public static JedisPool getPool() {
		if (JedisConfiguration.jedisPool == null) {

			JedisPoolConfig poolConfig = new JedisPoolConfig();
			// The max objects to be allocated by the pool. If total is reached, the pool is
			// exhausted.
			poolConfig.setMaxTotal(128);

			// The max objects to be idle in the pool
			poolConfig.setMaxIdle(128);

			// The min objects to be idle in the pool
			poolConfig.setMinIdle(16);

			// The minimum time that an object can be idle in the pool before eviction due
			// to idle
			poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());

			// The time for the thread which checks the idle objects to sleep
			poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());

			// It is going to block the connections until an idle instance becomes available
			poolConfig.setBlockWhenExhausted(true);

			JedisConfiguration.jedisPool = new JedisPool(poolConfig, "localhost");
		}

		return JedisConfiguration.jedisPool;
	}

}
