package com.maple.starter.shiro;

import com.maple.starter.shiro.jwt.DefaultShiroJwtAuthorization;
import com.maple.starter.shiro.jwt.JwtRealm;
import com.maple.starter.shiro.jwt.ShiroJwtAuthorization;
import com.maple.starter.shiro.properties.ShiroJwtProperties;
import com.maple.starter.shiro.utils.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.realm.Realm;
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
@AllArgsConstructor
@EnableConfigurationProperties(ShiroJwtProperties.class)
@AutoConfigureBefore({ShiroAutoConfiguration.class, ShiroWebFilterAutoConfiguration.class})
@ConditionalOnProperty(value = "mp.shiro.jwt.enable", havingValue = "true")
public class ShiroJwtAutoConfiguration {
    private final ShiroJwtProperties shiroJwtProperties;

    @Bean
    @ConditionalOnMissingBean(ShiroJwtAuthorization.class)
    public ShiroJwtAuthorization shiroJwtAuthorization() {
        return new DefaultShiroJwtAuthorization();
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtUtils jwtUtils() {
        return new JwtUtils(shiroJwtProperties, shiroJwtAuthorization());
    }

    @Bean
    @ConditionalOnMissingBean
    public Realm jwtRealm() {
        return new JwtRealm(jwtUtils());
    }
}
