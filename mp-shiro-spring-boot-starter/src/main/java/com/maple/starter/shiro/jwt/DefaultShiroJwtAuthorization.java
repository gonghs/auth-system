package com.maple.starter.shiro.jwt;

import cn.hutool.http.ContentType;
import com.maple.common.builder.Results;
import com.maple.common.exception.ErrorCode;
import com.maple.server.common.constant.GlobalConst;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 默认权限功能实现
 *
 * @author maple
 * @version 1.0
 * @since 2020-06-30 11:30
 */
public class DefaultShiroJwtAuthorization implements ShiroJwtAuthorization {
    @Override
    public String getSecret(String userId) {
        return null;
    }

    @Override
    public SimpleAuthorizationInfo getAuthorizationInfo(String userId) {
        return new SimpleAuthorizationInfo();
    }

    @Override
    public boolean verifyUser(String userId, String token) {
        return true;
    }

    @Override
    @SneakyThrows
    public void authenticationExceptionHandel(ServletRequest request, ServletResponse response,
                                              AuthenticationException authenticationException) {
        HttpServletResponse resp = WebUtils.toHttp(response);
        resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        @Cleanup PrintWriter out = resp.getWriter();
        resp.setCharacterEncoding(GlobalConst.DEFAULT_CHARSET_STR);
        resp.setContentType(ContentType.JSON.name());
        out.println(Results.failure(ErrorCode.AUTH_ERROR.code(), "token校验异常"));
    }

    @Override
    @SneakyThrows
    public void authorizationExceptionHandel(HttpServletRequest request, HttpServletResponse response,
                                             AuthorizationException authorizationException) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        @Cleanup PrintWriter out = response.getWriter();
        response.setCharacterEncoding(GlobalConst.DEFAULT_CHARSET_STR);
        response.setContentType(ContentType.JSON.name());
        out.println(Results.failure(ErrorCode.AUTH_ERROR.code(), "token校验异常"));
    }
}
