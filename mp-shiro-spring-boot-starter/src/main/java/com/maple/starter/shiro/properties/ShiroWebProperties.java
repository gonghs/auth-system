package com.maple.starter.shiro.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 权限相关配置
 *
 * @author maple
 * @version 1.0
 * @since 2020-03-11 14:00
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties("mp.shiro.web")
public class ShiroWebProperties {
    /**
     * 自定义过滤器
     */
    private Map<String, Filter> filters = new LinkedHashMap<>();
}
