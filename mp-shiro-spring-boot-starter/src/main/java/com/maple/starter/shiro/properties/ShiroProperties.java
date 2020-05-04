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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        /**
         * bean名称
         */
        private String name;
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
    public class AutoProxyCreator {
        /**
         * 是否开启
         */
        private Boolean enabled = true;
        /**
         * 是否在bean名称中包含特定前缀 此值设为false时 同时使用aop注解和Shiro权限注解会导致接口404异常
         */
        private Boolean usePrefix = true;
        /**
         * 是否启用类代理
         */
        private Boolean proxyTargetClass = true;
    }
}
