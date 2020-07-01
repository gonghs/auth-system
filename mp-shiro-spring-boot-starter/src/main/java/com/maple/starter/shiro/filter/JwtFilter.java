package com.maple.starter.shiro.filter;

import com.maple.starter.shiro.exception.AuthException;
import com.maple.starter.shiro.jwt.JwtToken;
import com.maple.starter.shiro.jwt.ShiroJwtAuthorization;
import com.maple.starter.shiro.utils.JwtUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Jwt过滤器 在shiro中注册名为jwt
 *
 * {@link com.maple.starter.shiro.ShiroWebFilterAutoConfiguration}
 * @author maple
 * @version 1.0
 * @since 2020-06-30 14:20
 */
public class JwtFilter extends BasicHttpAuthenticationFilter {

    private final ShiroJwtAuthorization shiroJwtAuthorization;
    private final JwtUtils jwtUtils;

    public JwtFilter(JwtUtils jwtUtils) {
        this.shiroJwtAuthorization = jwtUtils.getShiroJwtAuthorization();
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader(jwtUtils.getShiroJwtProperties().getHeaderKey());
        return authorization != null;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws AuthenticationException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader(jwtUtils.getShiroJwtProperties().getHeaderKey());
        authorization = authorization.replaceAll("(?i)" + jwtUtils.getShiroJwtProperties().getPrefix(), "");
        JwtToken token = new JwtToken(authorization);
        // 校验token
        String userId = jwtUtils.getUserId(authorization);
        if (userId == null) {
            return false;
        }
        getSubject(request, response).login(token);
        return true;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            try {
                return executeLogin(request, response);
            } catch (AuthenticationException e) {
                this.shiroJwtAuthorization.authenticationExceptionHandel(request, response, e);
                return false;
            }
        }
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws AuthenticationException {
        this.shiroJwtAuthorization.authenticationExceptionHandel(request, response, new AuthException("拒绝访问"));
        return false;
    }
}
