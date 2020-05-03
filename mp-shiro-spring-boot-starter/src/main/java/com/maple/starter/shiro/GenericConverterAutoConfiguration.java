package com.maple.starter.shiro;

import com.maple.starter.shiro.converter.String2ObjectGenericConverter;
import com.maple.starter.shiro.properties.ShiroProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.GenericConverter;

/**
 * 配置解析转换器自动配置
 *
 * @author maple
 * @version 1.0
 * @since 2020-04-29 16:24
 */
@Configuration
@EnableConfigurationProperties(ShiroProperties.class)
public class GenericConverterAutoConfiguration {
    /**
     * 此配置用于将String 转化为Object
     */
    @Bean
    @ConfigurationPropertiesBinding
    public GenericConverter string2ObjectGenericConverter() {
        return new String2ObjectGenericConverter();
    }
}
