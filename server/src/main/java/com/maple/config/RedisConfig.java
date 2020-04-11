package com.maple.server.config;

import com.maple.function.basic.FastJsonRedisSerializer;
import org.crazycake.shiro.RedisManager;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * redis配置
 *
 * @author maple
 * @version 1.0
 * @since 2020-03-31 11:20
 */
@Configuration
public class RedisConfig {
    private final RedisProperties redisProperties;

    public RedisConfig(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> stringObjectRedisTemplate = new RedisTemplate<>();
        stringObjectRedisTemplate.setConnectionFactory(factory);
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        stringObjectRedisTemplate.setKeySerializer(stringRedisSerializer);
        stringObjectRedisTemplate.setHashKeySerializer(stringRedisSerializer);
        stringObjectRedisTemplate.setValueSerializer(fastJsonRedisSerializer);
        stringObjectRedisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
        stringObjectRedisTemplate.afterPropertiesSet();
        return stringObjectRedisTemplate;
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 设置缓存有效期一小时
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(1));
        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration).build();
    }



    /**
     * redis缓存管理器
     */
    @Bean
    public org.crazycake.shiro.RedisCacheManager shiroRedisCacheManager() {
        org.crazycake.shiro.RedisCacheManager redisCacheManager = new org.crazycake.shiro.RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        // redis中针对不同用户缓存的标识字段
        redisCacheManager.setPrincipalIdFieldName("id");
        // 用户权限信息缓存时间
        redisCacheManager.setExpire(200000);
        return redisCacheManager;
    }

    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(String.format("%s:%s", redisProperties.getHost(), redisProperties.getPort()));
        redisManager.setPassword(redisProperties.getPassword());
        return redisManager;
    }
}
