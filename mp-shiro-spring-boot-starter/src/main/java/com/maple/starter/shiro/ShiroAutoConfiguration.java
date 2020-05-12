package com.maple.starter.shiro;

import cn.hutool.core.collection.CollUtil;
import com.maple.common.constant.SymbolConst;
import com.maple.starter.shiro.properties.ShiroProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.boot.autoconfigure.ShiroAnnotationProcessorAutoConfiguration;
import org.apache.shiro.spring.boot.autoconfigure.exception.NoRealmBeanConfiguredException;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebFilterConfiguration;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 自动配置类
 *
 * @author maple
 * @version 1.0
 * @since 2020-04-11 21:30
 */
@Slf4j
@Configuration
@AllArgsConstructor
@AutoConfigureBefore({org.apache.shiro.spring.boot.autoconfigure.ShiroAutoConfiguration.class,
        ShiroAnnotationProcessorAutoConfiguration.class, ShiroWebFilterConfiguration.class})
public class ShiroAutoConfiguration implements InitializingBean {
    private final ShiroProperties shiroProperties;
    private final AtomicLong counter = new AtomicLong(0);
    private final GenericApplicationContext genericApplicationContext;

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
     * // 猜测原因: （尚未验证）
     * (userPrefix将实际的bean名称进行了变更因此aop和shiro取到的不是同一个对象 而proxyTargetClass基于类代理不同的代理方式也能解决代理冲突)
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "mp.shiro.auto-proxy-creator.enabled", havingValue = "true", matchIfMissing = true)
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setUsePrefix(shiroProperties.getAdvisorAutoProxyCreator().getUsePrefix());
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(shiroProperties.getAdvisorAutoProxyCreator().getProxyTargetClass());
        Optional.ofNullable(shiroProperties.getAdvisorAutoProxyCreator().getBeanName()).ifPresent(defaultAdvisorAutoProxyCreator::setBeanName);
        return defaultAdvisorAutoProxyCreator;
    }


    /**
     * FIXME 之后尝试解决这个问题
     * 先声明一个占位的bean 原因在于使用registerContainer注入的bean 无法被ConditionalOnMissingBean识别
     * (也可能是springboot是否注入bean是一开始就确定的 而不是执行前尝试获取bean才确定)
     * 也就是说这里本质上是防止 org.apache.shiro.spring.boot.autoconfigure.ShiroAutoConfiguration抛出异常
     */
    @Bean
    @ConditionalOnMissingBean
    public Realm missRealm() {
        if (CollUtil.isEmpty(shiroProperties.realms())) {
            throw new NoRealmBeanConfiguredException();
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() {
        // init realms
        shiroProperties.realms().forEach(item -> {
            String name =
                    Optional.ofNullable(item.getName()).orElse(item.getClass().getName() + Objects.toString(counter.incrementAndGet()));
            genericApplicationContext.registerBean(name, Realm.class, () -> item);
        });
        // 猜测与org.apache.shiro.spring.config.web.autoconfigure FilterRegistrationBean自动配置有关
        // 使用此方式自动注册之后会使过滤链顺序失效 凡带有 /**:[filter] 的无视所有规则拦截所有
        //        shiroProperties.getFilters().forEach((key,val) -> genericApplicationContext.registerBean(key,
        //        Filter.class, () -> val));
    }
}
