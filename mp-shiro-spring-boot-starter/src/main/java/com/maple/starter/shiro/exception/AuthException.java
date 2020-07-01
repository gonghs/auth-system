package com.maple.starter.shiro.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 权限异常的异常
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-10 10:41
 */
public class AuthException extends AuthenticationException {
    private static final long serialVersionUID = -1156951780670243758L;

    public AuthException() {
    }

    public AuthException(String msg) {
        super(msg);
    }
}
