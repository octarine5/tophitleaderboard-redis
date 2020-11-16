package com.example.leaderboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Configuration
@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }
}

/*
 * https://www.baeldung.com/jedis-java-redis-client-library
 * https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/connection/jedis/JedisConnectionFactory.html
 * https://www.baeldung.com/spring-data-redis-tutorial
 * https://spring.io/projects/spring-data-redis
 * https://docs.spring.io/spring-data/data-redis/docs/2.1.5.RELEASE/reference/html/#new-in-2.1.0
 */