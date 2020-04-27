package com.maple.starter.shiro;

import com.maple.starter.shiro.properties.ShiroRedisProperties;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.web.config.AbstractShiroWebConfiguration;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.List;

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
@EnableConfigurationProperties({ShiroRedisProperties.class, RedisProperties.class})
public class ShiroRedisAutoConfiguration extends AbstractShiroWebConfiguration {
    private final RedisProperties redisProperties;
    private final ShiroRedisProperties shiroRedisProperties;

    public ShiroRedisAutoConfiguration(RedisProperties redisProperties, ShiroRedisProperties shiroRedisProperties) {
        this.redisProperties = redisProperties;
        this.shiroRedisProperties = shiroRedisProperties;
    }

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

    @Lazy
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RedisProperties.class)
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(String.format("%s:%s", redisProperties.getHost(), redisProperties.getPort()));
        redisManager.setPassword(redisProperties.getPassword());
        return redisManager;
    }

    @Bean
    @ConditionalOnMissingBean
    public SessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    @Bean
    @Override
    @ConditionalOnMissingBean
    protected SessionsSecurityManager securityManager(List<Realm> realms) {
        DefaultWebSessionManager webSessionManager = new DefaultWebSessionManager();
        webSessionManager.setSessionIdCookieEnabled(sessionIdCookieEnabled);
        webSessionManager.setSessionIdUrlRewritingEnabled(sessionIdUrlRewritingEnabled);
        webSessionManager.setSessionIdCookie(sessionCookieTemplate());

        webSessionManager.setSessionFactory(sessionFactory());
        // 替换为redis
        webSessionManager.setSessionDAO(redisSessionDAO());
        webSessionManager.setDeleteInvalidSessions(sessionManagerDeleteInvalidSessions);
        SessionsSecurityManager securityManager = super.securityManager(realms);
        securityManager.setSessionManager(webSessionManager);
        return securityManager;
    }
}
