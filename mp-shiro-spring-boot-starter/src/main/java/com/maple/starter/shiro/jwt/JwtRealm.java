package com.maple.starter.shiro.jwt;

import com.maple.starter.shiro.exception.AuthException;
import com.maple.starter.shiro.utils.JwtUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * UserRealm
 * 登录由{@link com.maple.starter.shiro.filter.JwtFilter}根据头部token获取Token对象发起,
 * 然后在此realm进行实际登录行为
 *
 * @author maple
 * @version 1.0
 * @since 2020-06-30 14:41
 */
public class JwtRealm extends AuthorizingRealm {
    private final ShiroJwtAuthorization shiroJwtAuthorization;
    private final JwtUtils jwtUtils;

    public JwtRealm(JwtUtils jwtUtils) {
        this.shiroJwtAuthorization = jwtUtils.getShiroJwtAuthorization();
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String userId = jwtUtils.getUserId(principals.toString());
        return shiroJwtAuthorization.getAuthorizationInfo(userId);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        String userId = jwtUtils.getUserId(token);
        if (userId == null) {
            throw new AuthException("token无效!");
        }
        if (!shiroJwtAuthorization.verifyUser(userId, token)) {
            throw new AuthException("用户不匹配!");
        }
        if (!jwtUtils.verify(token, userId)) {
            throw new AuthException("token校验有误!");
        }
        return new SimpleAuthenticationInfo(token, token, "jwt_realm");
    }
}
