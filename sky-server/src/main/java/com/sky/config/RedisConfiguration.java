package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Author: leiwenfeng
 * Date: 2023/9/9 2:52
 */
@Configuration
@Slf4j
public class RedisConfiguration {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Bean
    public RedisTemplate redisTemplate(RedisTemplate redisTemplate ,RedisConnectionFactory redisConnectionFactory){
        log.info("开始创建redis模板对象redisTemplate");

        redisTemplate.setConnectionFactory(redisConnectionFactory);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);

        log.info("成功创建redis模板对象redisTemplate");
        return redisTemplate;

    }
}
