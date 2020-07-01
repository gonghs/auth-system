package com.maple.starter.shiro.jwt;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.SimpleAuthorizationInfo;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * shiro jwt权限功能接口 默认实现为{@link DefaultShiroJwtAuthorization}
 * 通常需要自定义
 *
 * @author maple
 * @version 1.0
 * @since 2020-06-30 11:27
 */
public interface ShiroJwtAuthorization {
    /**
     * 根据用户获取秘钥
     *
     * @param userId user id
     * @return secret
     */
    String getSecret(String userId);

    /**
     * 获取权限对象,定义权限
     *
     * @param userId user id
     * @return authorizationInfo
     */
    SimpleAuthorizationInfo getAuthorizationInfo(String userId);

    /**
     * 用户校验
     *
     * @param userId user id
     * @param token  token
     * @return boolean
     */
    boolean verifyUser(String userId, String token);

    /**
     * 异常拦截
     *
     * @param request                 ServletRequest
     * @param response                ServletResponse
     * @param authenticationException AuthenticationException
     */
    void authenticationExceptionHandel(ServletRequest request, ServletResponse response,
                                       AuthenticationException authenticationException);

    /**
     * 异常拦截器
     *
     * @param request                ServletRequest
     * @param response               ServletResponse
     * @param authorizationException AuthenticationException
     */
    void authorizationExceptionHandel(HttpServletRequest request, HttpServletResponse response,
                                      AuthorizationException authorizationException);
}
