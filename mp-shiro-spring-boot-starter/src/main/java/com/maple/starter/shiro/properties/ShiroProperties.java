package com.maple.starter.shiro.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 权限相关配置
 *
 * @author maple
 * @version 1.0
 * @since 2020-03-11 14:00
 */
@Data
@Configuration
@ConfigurationProperties("mp.shiro")
public class ShiroProperties {
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


    @Getter
    @Setter
    public class RealmDefinition {
        /**
         * realm原型
         */
        private RealmProperty realm;
    }

    @Getter
    @Setter
    public class RealmProperty {
        /**
         * 类对象
         */
        private Realm target;
        private Map<String, Object> property;
        private CredentialsMatcherDefinition credentialsMatcher;

        @Getter
        @Setter
        class CredentialsMatcherDefinition {
            private CredentialsMatcher target;
            private Map<String, Object> property;
        }
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
