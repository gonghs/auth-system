package com.maple.server.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.maple.server.common.constant.SecurityConst;
import com.maple.server.common.constant.SymbolConst;
import com.maple.server.common.entity.MyRealm;
import com.maple.server.function.filter.CustomAccessControlFilter;
import com.maple.server.function.filter.CustomUserFilter;
import com.maple.server.properties.AuthProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro 配置
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-09 19:18
 */
@Configuration
@Slf4j
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class ShiroConfig {
    private final AuthProperties authProperties;
    @Autowired
    private RedisCacheManager shiroRedisCacheManager;

    public ShiroConfig(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件过滤器
     * </br>1,配置shiro安全管理器接口securityManage;
     * </br>2,shiro 连接约束配置filterChainDefinitions;
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();

        log.info("Shiro拦截器工厂类注入开始");

        // 配置shiro安全管理器 SecurityManager
        bean.setSecurityManager(securityManager);
        //添加kick out认证
        Map<String, Filter> hashMap = new HashMap<>(2);
        hashMap.put("kickout", myAccessControlFilter());
        hashMap.put("authc", new CustomUserFilter());
        bean.setFilters(hashMap);

        // 指定要求登录时的链接
        bean.setLoginUrl(authProperties.getLoginUrl());
        // 登录成功后要跳转的链接
        bean.setSuccessUrl(authProperties.getSuccessUrl());
        // 未授权时跳转的界面;
        bean.setUnauthorizedUrl(authProperties.getUnauthorizedUrl());

        // filterChainDefinitions拦截器map必须用：LinkedHashMap，因为它必须保证有序
        Map<String, String> filterMap = new LinkedHashMap<>();
        final int two = 2;
        authProperties.getFilterChain().forEach(item -> {
            String[] chain = item.split(SymbolConst.COLON);
            if (chain.length == two) {
                filterMap.put(chain[0], chain[1]);
            } else {
                // 否则默认作为放行路径
                filterMap.put(item, "anon");
            }
        });

        // 添加 shiro 过滤器
        bean.setFilterChainDefinitionMap(filterMap);
        return bean;
    }

    /**
     * shiro安全管理器设置realm认证和自定义缓存管理
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();

        // 关联realm
        manager.setRealm(shiroRealm());
        // 自定义redis缓存
        manager.setCacheManager(shiroRedisCacheManager);
        // 注入session管理器;
        manager.setSessionManager(sessionManager());
        // 注入Cookie记住我管理器
        manager.setRememberMeManager(rememberMeManager());

        return manager;
    }

    /**
     * 3.创建身份认证 Realm
     */
    @Bean
    public MyRealm shiroRealm() {
        MyRealm realm = new MyRealm();
        realm.setCacheManager(shiroRedisCacheManager);
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


    /**
     * RememberMe cookie
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        log.info("记住我，设置cookie过期时间！");
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        // //记住我cookie生效时间30天 ,单位秒  [10天]
        cookie.setMaxAge(864000);
        // 设置只读模型
        return cookie;
    }

    /**
     * 配置cookie记住我管理器
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        log.debug("配置cookie记住我管理器！");
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }


    /**
     * 访问控制过滤器
     */
    private CustomAccessControlFilter myAccessControlFilter() {
        CustomAccessControlFilter customAccessControlFilter = new CustomAccessControlFilter();
        //使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
        //这里我们还是用之前shiro使用的ehcache实现的cacheManager()缓存管理
        //也可以重新另写一个，重新配置缓存时间之类的自定义缓存属性
        customAccessControlFilter.setCacheManager(shiroRedisCacheManager);
        //用于根据会话ID，获取会话进行踢出操作的；
        customAccessControlFilter.setSessionManager(sessionManager());
        //是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；踢出顺序。
        customAccessControlFilter.setKickOutAfter(false);
        //同一个用户最大的会话数，默认1；比如2的意思是同一个用户允许最多同时两个人登录；
        customAccessControlFilter.setMaxSession(1);
        //被踢出后重定向到的地址
        customAccessControlFilter.setKickOutUrl(String.format("%s?%s=1", SecurityConst.LOGIN_URL,
                SecurityConst.KICK_OUT_ATTRIBUTE_KEY));
        return customAccessControlFilter;
    }


    /**
     * sessionManager添加session缓存操作DAO
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(enterCacheSessionDAO());
        sessionManager.setSessionIdCookie(sessionIdCookie());
        return sessionManager;
    }

    /**
     * EnterpriseCacheSessionDAO shiro sessionDao层的实现；
     * 提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
     */
    @Bean
    public EnterpriseCacheSessionDAO enterCacheSessionDAO() {
        EnterpriseCacheSessionDAO enterCacheSessionDAO = new EnterpriseCacheSessionDAO();
        enterCacheSessionDAO.setCacheManager(shiroRedisCacheManager);
        enterCacheSessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        return enterCacheSessionDAO;
    }


    /**
     * 自定义cookie中session名称等配置
     */
    @Bean
    public SimpleCookie sessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
        //如果在Cookie中设置了"HttpOnly"属性，那么通过程序(JS脚本、Applet等)将无法读取到Cookie信息，这样能有效的防止XSS攻击。
        simpleCookie.setHttpOnly(true);
        simpleCookie.setName("redisShiroSessionId");
        //单位秒
        simpleCookie.setMaxAge(86400);
        return simpleCookie;
    }


    /**
     * shiro 方言配置
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        /*
          setUsePrefix(false)用于解决一个奇怪的bug。在引入spring aop的情况下。
          在@Controller注解的类的方法中加入@RequiresRole等shiro注解，会导致该方法无法映射请求，导致返回404。
          加入这项配置能解决这个bug
         */
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }
}
