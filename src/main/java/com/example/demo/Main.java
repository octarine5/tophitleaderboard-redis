package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

@Configuration
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }


}

/*
 * https://www.baeldung.com/jedis-java-redis-client-library
 * https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/connection/jedis/JedisConnectionFactory.html
 * https://www.baeldung.com/spring-data-redis-tutorial
 * https://spring.io/projects/spring-data-redis
 * https://docs.spring.io/spring-data/data-redis/docs/2.1.5.RELEASE/reference/html/#new-in-2.1.0
 */