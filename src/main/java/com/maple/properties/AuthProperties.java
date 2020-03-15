package com.maple.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 权限相关配置
 *
 * @author gonghs
 * @version 1.0
 * @since 2020-03-11 14:00
 */
@Data
@Configuration
@ConfigurationProperties("auth")
public class AuthProperties {
    /**
     * 登录路径
     */
    private String loginUrl;
    /**
     * 登录成功跳转路径
     */
    private String successUrl;
    /**
     * 无权限路径
     */
    private String unauthorizedUrl;
    /**
     * 过滤链
     */
    private List<String> filterChain;
}
