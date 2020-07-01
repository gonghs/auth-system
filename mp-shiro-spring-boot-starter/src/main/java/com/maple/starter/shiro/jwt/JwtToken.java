package com.maple.starter.shiro.jwt;

import lombok.AllArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * Jwt登录token对象
 *
 * @author maple
 * @version 1.0
 * @since 2020-06-30 14:41
 */
@AllArgsConstructor
public class JwtToken implements AuthenticationToken {
    private final String token;

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
