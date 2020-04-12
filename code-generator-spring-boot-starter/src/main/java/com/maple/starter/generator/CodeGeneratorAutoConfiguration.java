package com.maple.starter.generator;

import com.maple.starter.generator.properties.CodeGeneratorProperties;
import com.maple.starter.generator.properties.DbProperties;
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
@EnableConfigurationProperties({CodeGeneratorProperties.class, DbProperties.class})
public class CodeGeneratorAutoConfiguration {
    @Bean
    public CodeGeneratorUtils codeGeneratorUtils(DbProperties dbProperties,
                                                 CodeGeneratorProperties codeGeneratorProperties) {
        return new CodeGeneratorUtils(dbProperties, codeGeneratorProperties);
    }
}
