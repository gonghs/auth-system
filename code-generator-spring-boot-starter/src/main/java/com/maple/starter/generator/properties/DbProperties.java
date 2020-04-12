package com.maple.starter.generator.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Optional;

/**
 * 数据源配置
 *
 * @author maple
 * @version 1.0
 * @since 2020-04-12 14:40
 */
@Data
@ConfigurationProperties(prefix = "spring.datasource")
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
    private DruidProperties druid = new DruidProperties();

    @Getter
    @Setter
    public class DruidProperties {
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

    public String getUrl() {
        return Optional.ofNullable(url).orElse(druid.getUrl());
    }

    public String getUsername() {
        return Optional.ofNullable(username).orElse(druid.getUsername());
    }

    public String getPassword() {
        return Optional.ofNullable(password).orElse(druid.getPassword());
    }

    public String getDriverClassName() {
        return Optional.ofNullable(driverClassName).orElse(druid.getDriverClassName());
    }
}
