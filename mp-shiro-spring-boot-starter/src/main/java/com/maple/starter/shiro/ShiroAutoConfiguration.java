package com.maple.starter.shiro;

import com.maple.starter.shiro.properties.AuthProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties(AuthProperties.class)
public class ShiroAutoConfiguration {
    @Autowired
    private AuthProperties authProperties;
    /**
     * ShiroFilterFactoryBean 处理拦截资源文件过滤器
     * </br>1,配置shiro安全管理器接口securityManage;
     * </br>2,shiro 连接约束配置filterChainDefinitions;
     */
//    @Bean
//    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
//        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
//
//        log.info("Shiro拦截器工厂类注入开始");
//
//        // 配置shiro安全管理器 SecurityManager
//        bean.setSecurityManager(securityManager);
//        //添加kick out认证
//        Map<String, Filter> hashMap = new HashMap<>(2);
//        hashMap.put("kickout", myAccessControlFilter());
//        hashMap.put("authc", new CustomUserFilter());
//        bean.setFilters(hashMap);
//
//        // 指定要求登录时的链接
//        bean.setLoginUrl(authProperties.getLoginUrl());
//        // 登录成功后要跳转的链接
//        bean.setSuccessUrl(authProperties.getSuccessUrl());
//        // 未授权时跳转的界面;
//        bean.setUnauthorizedUrl(authProperties.getUnauthorizedUrl());
//
//        // filterChainDefinitions拦截器map必须用：LinkedHashMap，因为它必须保证有序
//        Map<String, String> filterMap = new LinkedHashMap<>();
//        final int two = 2;
//        authProperties.getFilterChain().forEach(item -> {
//            String[] chain = item.split(SymbolConst.COLON);
//            if (chain.length == two) {
//                filterMap.put(chain[0], chain[1]);
//            } else {
//                // 否则默认作为放行路径
//                filterMap.put(item, "anon");
//            }
//        });
//
//        // 添加 shiro 过滤器
//        bean.setFilterChainDefinitionMap(filterMap);
//        return bean;
//    }
}
