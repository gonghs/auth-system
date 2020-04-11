package com.maple.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 数据库配置读取
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-09 00:23
 */
@Data
@Configuration
@ConfigurationProperties("spring.datasource.druid")
public class DbProperties {
    /**
     * 连接url
     */
    private String url;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 驱动类名
     */
    private String driverClassName;
}
