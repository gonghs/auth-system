package com.maple.server.config;

import com.maple.server.common.entity.MyRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.Realm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * shiro 配置
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-09 19:18
 */
@Configuration
@Slf4j
public class ShiroConfig {

    /**
     * 3.创建身份认证 Realm
     */
    @Bean
    public Realm shiroRealm(CacheManager cacheManager) {
        MyRealm realm = new MyRealm();
        realm.setCacheManager(cacheManager);
        realm.setCredentialsMatcher(hashedCredentialsMatcher());
        return realm;
    }


    /**
     * 凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     * 所以我们需要修改下doGetAuthenticationInfo中的代码,更改密码生成规则和校验的逻辑一致即可; ）
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        // 散列的次数，比如散列两次，相当于 // md5(md5(""));
        hashedCredentialsMatcher.setHashIterations(2);
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }
}
