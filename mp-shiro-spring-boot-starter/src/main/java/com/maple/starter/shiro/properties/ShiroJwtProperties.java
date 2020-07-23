package com.maple.starter.shiro.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * jwt配置
 *
 * @author maple
 * @version 1.0
 * @since 2020-06-30 11:41
 */
@Data
@ConfigurationProperties("mp.shiro.jwt")
public class ShiroJwtProperties {
    private Boolean enable = false;
    /**
     * 秘钥
     */
    private String secret = "";
    /**
     * 过期时间
     */
    private Long expire = 200000L;
    /**
     * token 前缀
     */
    private String prefix = "Bearer ";
    /**
     * token签名前缀
     */
    private String signPrefix = prefix;
    /**
     * token在head的key
     */
    private String headerKey = "Authorization";
}
