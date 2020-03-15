package com.maple.common.exception;

import javax.security.auth.login.LoginException;

/**
 * 权限异常的异常
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-10 10:41
 */
public class AuthException extends LoginException {
    private static final long serialVersionUID = -1156951780670243758L;

    public AuthException() {
    }

    public AuthException(String msg) {
        super(msg);
    }
}
