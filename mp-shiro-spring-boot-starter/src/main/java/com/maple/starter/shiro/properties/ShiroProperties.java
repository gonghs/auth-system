package com.maple.starter.shiro.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 权限相关配置
 *
 * @author maple
 * @version 1.0
 * @since 2020-03-11 14:00
 */
@Data
@Configuration
@ConfigurationProperties("mp.shiro")
public class ShiroProperties {
    /**
     * 过滤链
     */
    private List<String> filterChain;
}
