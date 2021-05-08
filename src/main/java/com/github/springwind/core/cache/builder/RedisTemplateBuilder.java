package com.github.springwind.core.cache.builder;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.Charset;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/9/16 15:29
 * @Desc
 */
@Component
public class RedisTemplateBuilder {

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    public <T> RedisTemplate<String, T> build(Class<T> tClass){
        RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(tClass));
        redisTemplate.setDefaultSerializer(new StringRedisSerializer(Charset.defaultCharset()));
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
