package com.maple.starter.shiro;

import com.maple.common.constant.SymbolConst;
import com.maple.starter.shiro.properties.ShiroProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.spring.boot.autoconfigure.ShiroAnnotationProcessorAutoConfiguration;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@AutoConfigureBefore({ShiroRedisAutoConfiguration.class, ShiroAnnotationProcessorAutoConfiguration.class})
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


    /**
     * 由于多重代理的原因 如果userPrefix和proxyTargetClass都为false会导致 aop和shiro权限注解不兼容 资源报错404
     * 因此两个属性至少需要其中一个属性为true才可以
     * // 猜测结果: 尚未验证
     * (userPrefix将实际的bean名称进行了变更因此aop和shiro取到的不是同一个对象 而proxyTargetClass基于类代理不同的代理方式也能解决代理冲突)
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "mp.shiro.auto-proxy-creator.enabled", havingValue = "true", matchIfMissing = true)
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setUsePrefix(shiroProperties.getAdvisorAutoProxyCreator().getUsePrefix());
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(shiroProperties.getAdvisorAutoProxyCreator().getProxyTargetClass());
        return defaultAdvisorAutoProxyCreator;
    }
}
