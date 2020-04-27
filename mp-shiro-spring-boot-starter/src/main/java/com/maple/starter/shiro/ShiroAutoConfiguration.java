package com.maple.starter.shiro;

import com.maple.common.constant.SymbolConst;
import com.maple.starter.shiro.properties.ShiroProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动配置类
 *
 * @author maple
 * @version 1.0
 * @since 2020-04-11 21:30
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(ShiroProperties.class)
public class ShiroAutoConfiguration {
    private final ShiroProperties shiroProperties;

    public ShiroAutoConfiguration(ShiroProperties shiroProperties) {
        this.shiroProperties = shiroProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        final int two = 2;
        shiroProperties.getFilterChain().forEach(item -> {
            String[] chain = item.split(SymbolConst.COLON);
            if (chain.length == two) {
                chainDefinition.addPathDefinition(chain[0], chain[1]);
            } else {
                // 否则默认作为放行路径
                chainDefinition.addPathDefinition(item, "anon");
            }
        });
        return chainDefinition;
    }
}
