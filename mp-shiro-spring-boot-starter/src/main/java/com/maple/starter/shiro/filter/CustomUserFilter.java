package com.maple.starter.shiro.filter;

import com.maple.starter.shiro.utils.RequestUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义用户过滤器 用户判断过滤未登陆的ajax请求 让其不返回页面
 *
 * @author maple
 * @version 1.0
 * @since 2019-10-04 14:34
 */
@Slf4j
public class CustomUserFilter extends FormAuthenticationFilter {

    @Override
    @SneakyThrows
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                log.info("Login submission detected.  Attempting to execute login.");
                return executeLogin(request, response);
            } else {
                log.info("Login page view.");
                return true;
            }
        } else {
            HttpServletRequest httpRequest = WebUtils.toHttp(request);
            if (RequestUtils.isAjax(httpRequest)) {
                HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
                httpServletResponse.sendError(401);
                return false;
            } else {
                log.info("This page is accessed without authorization. You will return to the login page. " +
                                "RequestUri:{} LoginPageUri:{}  ",
                        httpRequest.getRequestURI(), getLoginUrl());
                saveRequestAndRedirectToLogin(request, response);
            }

            return false;
        }
    }
}
