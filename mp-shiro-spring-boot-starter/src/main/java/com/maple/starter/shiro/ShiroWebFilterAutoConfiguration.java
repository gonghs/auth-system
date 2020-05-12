package com.maple.starter.shiro;

import com.maple.starter.shiro.properties.ShiroWebProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebFilterConfiguration;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.AbstractShiroWebFilterConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Map;

/**
 * shiro filter配置
 *
 * @author maple
 * @version 1.0
 * @since 2020-05-12 21:45
 */
@Slf4j
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(ShiroWebProperties.class)
@AutoConfigureBefore(ShiroWebFilterConfiguration.class)
@ConditionalOnProperty(value = "mp.shiro.web.enabled", havingValue = "true", matchIfMissing = true)
public class ShiroWebFilterAutoConfiguration extends AbstractShiroWebFilterConfiguration {
    private final ShiroWebProperties shiroWebProperties;

    @Bean
    @ConditionalOnMissingBean
    @Override
    protected ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = super.shiroFilterFactoryBean();
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        filters.putAll(shiroWebProperties.getFilters());
        return shiroFilterFactoryBean;
    }
}
