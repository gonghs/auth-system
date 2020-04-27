package com.maple.starter.shiro;

import com.maple.starter.shiro.properties.ShiroRedisProperties;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.spring.boot.autoconfigure.ShiroAutoConfiguration;
import org.crazycake.shiro.RedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * shiroRedis自动配置
 *
 * @author maple
 * @version 1.0
 * @since 2020-04-27 15:03
 */
@Configuration
@ConditionalOnProperty(value = "mp.shiro.cache.redis.enabled", havingValue = "true")
@AutoConfigureAfter(RedisAutoConfiguration.class)
@AutoConfigureBefore(ShiroAutoConfiguration.class)
@EnableConfigurationProperties(ShiroRedisProperties.class)
public class ShiroRedisAutoConfiguration {
    @Autowired
    private RedisProperties redisProperties;
    @Autowired
    private ShiroRedisProperties shiroRedisProperties;
    /**
     * redis缓存管理器
     */
    @Bean
    @ConditionalOnMissingBean
    public CacheManager cacheManager() {
        org.crazycake.shiro.RedisCacheManager redisCacheManager = new org.crazycake.shiro.RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        // redis中针对不同用户缓存的标识字段
        redisCacheManager.setPrincipalIdFieldName(shiroRedisProperties.getPrincipalIdFieldName());
        // 用户权限信息缓存时间
        redisCacheManager.setExpire(shiroRedisProperties.getExpire());
        return redisCacheManager;
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(String.format("%s:%s", redisProperties.getHost(), redisProperties.getPort()));
        redisManager.setPassword(redisProperties.getPassword());
        return redisManager;
    }
}
