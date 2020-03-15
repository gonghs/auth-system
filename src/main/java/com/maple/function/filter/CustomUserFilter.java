package com.maple.function.filter;

import com.maple.common.exception.AuthException;
import com.maple.utils.RequestUtils;
import lombok.SneakyThrows;
import org.apache.shiro.web.filter.authc.UserFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 自定义用户过滤器 用户判断过滤未登陆的ajax请求 让其不返回页面
 *
 * @author maple
 * @version 1.0
 * @since 2019-10-04 14:34
 */
public class CustomUserFilter extends UserFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return super.isAccessAllowed(request, response, mappedValue);
    }

    @Override
    @SneakyThrows
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        if (RequestUtils.isAjax(request)) {
            throw new AuthException("访问失败了,您尚未登陆哦!");
        } else {
            redirectToLogin(request, response);
        }
        return false;
    }
}
