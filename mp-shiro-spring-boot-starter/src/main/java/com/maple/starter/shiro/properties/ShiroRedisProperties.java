package com.maple.starter.shiro.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 权限相关配置
 *
 * @author maple
 * @version 1.0
 * @since 2020-03-11 14:00
 */
@Data
@Configuration
@ConfigurationProperties("mp.shiro.cache.redis")
public class ShiroRedisProperties {
    /**
     * 是否开启
     */
    private Boolean enabled;
    /**
     * 用于标识不同用户的唯一标识字段
     */
    private String principalIdFieldName;
    /**
     * 过期时间
     */
    private Integer expire = 200000;
}
