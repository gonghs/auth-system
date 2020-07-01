package com.maple.starter.shiro.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.Realm;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import javax.servlet.Filter;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

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
@ConfigurationProperties("mp.shiro")
public class ShiroProperties implements InitializingBean {
    private final GenericApplicationContext genericApplicationContext;
    private final AtomicLong counter = new AtomicLong(0);
    /**
     * 过滤链
     */
    private List<String> filterChain;
    /**
     * realm 列表
     */
    private List<RealmDefinition> realms = new ArrayList<>();
    /**
     * 自动代理构建器
     */
    private AutoProxyCreator advisorAutoProxyCreator = new AutoProxyCreator();

    public List<Realm> realms() {
        return realms.stream()
                .map(realmDefinition -> realmDefinition.getRealm().getTarget())
                .collect(Collectors.toList());
    }

    @Override
    public void afterPropertiesSet() {
        for (ShiroProperties.RealmDefinition realmDefinition : this.getRealms()) {
            setRealmProperty(realmDefinition);
            setCredentialsMatcherProperty(realmDefinition);
            setReamCredentialsMatcher(realmDefinition);
            // 注册bean
            String name =
                    Optional.ofNullable(realmDefinition.getRealm().getTarget().getName()).orElse(realmDefinition.getRealm().getTarget().getClass().getName() + Objects.toString(counter.incrementAndGet()));
            genericApplicationContext.registerBean(name, Realm.class, () -> realmDefinition.getRealm().getTarget());
        }
    }

    private void setReamCredentialsMatcher(RealmDefinition realmDefinition) {
        if (realmDefinition.getRealm().getTarget() instanceof AuthenticatingRealm) {
            ((AuthenticatingRealm) realmDefinition.getRealm().getTarget()).setCredentialsMatcher(realmDefinition.getRealm().getCredentialsMatcher().getTarget());
        }
    }

    private void setCredentialsMatcherProperty(RealmDefinition realmDefinition) {
        CredentialsMatcherDefinition credentialsMatcherDefinition =
                realmDefinition.getRealm().getCredentialsMatcher();
        CredentialsMatcher matcher = credentialsMatcherDefinition.getTarget();
        credentialsMatcherDefinition.getProperty().forEach((propertyName, propertyValue) -> {
            try {
                BeanUtils.setProperty(matcher, propertyName, propertyValue);
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error("设置CredentialsMatcher属性失败", e);
            }
        });
    }

    private void setRealmProperty(RealmDefinition realmDefinition) {
        realmDefinition.getRealm().getProperty().forEach((propertyName, propertyValue) -> {
            try {
                BeanUtils.setProperty(realmDefinition, propertyName, propertyValue);
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error("设置Realm属性失败", e);
            }
        });
    }

    /**
     * filter定义
     */
    @Getter
    @Setter
    public static class FilterDefinition {
        private String name;
        private Filter filter;
    }


    @Getter
    @Setter
    public static class RealmDefinition {
        /**
         * realm原型
         */
        private RealmProperty realm;
    }

    @Getter
    @Setter
    public static class RealmProperty {
        /**
         * 类对象
         */
        private Realm target;
        private Map<String, Object> property = new HashMap<>();
        private CredentialsMatcherDefinition credentialsMatcher = new CredentialsMatcherDefinition();
    }


    @Getter
    @Setter
    public static class CredentialsMatcherDefinition {
        private CredentialsMatcher target;
        private Map<String, Object> property = new HashMap<>();
    }

    @Getter
    @Setter
    public static class AutoProxyCreator {
        /**
         * 是否开启
         */
        private Boolean enabled = true;
        /**
         * 是否在bean名称中包含特定前缀 此值设为false时 同时使用aop注解和Shiro权限注解会导致接口404异常
         */
        private Boolean usePrefix = true;
        /**
         * bean名称
         */
        private String beanName;
        /**
         * 是否启用类代理
         */
        private Boolean proxyTargetClass = true;
    }
}
