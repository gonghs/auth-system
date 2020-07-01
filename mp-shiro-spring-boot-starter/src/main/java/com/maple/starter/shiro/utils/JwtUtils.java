package com.maple.starter.shiro.utils;


import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.maple.starter.shiro.jwt.ShiroJwtAuthorization;
import com.maple.starter.shiro.properties.ShiroJwtProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

import static org.apache.shiro.SecurityUtils.getSubject;


/**
 * JWT 工具
 *
 * @author maple
 * @version 1.0
 * @since 2020-06-30 14:20
 */
@Data
@AllArgsConstructor
public class JwtUtils {
    private final ShiroJwtProperties shiroJwtProperties;
    private final ShiroJwtAuthorization shiroJwtAuthorization;

    /**
     * 校验token
     *
     * @param token  token
     * @param userId userId
     * @return true or false
     */
    public boolean verify(String token, String userId) {
        try {
            token = remotePrefix(token);
            String secret = shiroJwtAuthorization.getSecret(userId) == null ? shiroJwtProperties.getSecret() : null;
            assert secret != null;
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withSubject(userId)
                    .build();
            verifier.verify(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 获取userId
     *
     * @return user id
     */
    public String getUserId() {
        try {
            DecodedJWT jwt = JWT.decode(getSubject().getPrincipal().toString());
            return jwt.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取userId
     *
     * @param token token
     * @return userId
     */
    public String getUserId(String token) {
        try {
            token = remotePrefix(token);
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 生成token
     *
     * @param userId userId
     * @return token
     */
    public String sign(String userId) {
        Date date = new Date(System.currentTimeMillis() + shiroJwtProperties.getExpireTime());
        String secret = shiroJwtAuthorization.getSecret(userId) == null ? shiroJwtProperties.getSecret() : null;
        assert secret != null;
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return shiroJwtProperties.getSignPrefix() + JWT.create()
                .withSubject(userId)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    /**
     * 去除前缀
     *
     * @param token token
     * @return token
     */
    private String remotePrefix(String token) {
        Objects.requireNonNull(token);
        if (token.startsWith(shiroJwtProperties.getPrefix())) {
            token = StrUtil.subAfter(token, shiroJwtProperties.getPrefix(), false);
        }
        return token;
    }
}
