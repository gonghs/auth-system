package com.maple.starter.generator;

import com.maple.starter.generator.properties.CodeGeneratorProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动配置类
 *
 * @author gonghs
 * @version 1.0
 * @since 2020-04-11 21:30
 */
@Configuration
@EnableConfigurationProperties(CodeGeneratorProperties.class)
public class CodeGeneratorAutoConfiguration {
    @Bean
    @ConditionalOnBean(DataSourceProperties.class)
    public CodeGeneratorUtils codeGeneratorUtils(DataSourceProperties dbProperties,CodeGeneratorProperties codeGeneratorProperties) {
        return new CodeGeneratorUtils(dbProperties, codeGeneratorProperties);
    }
}
