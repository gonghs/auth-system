package com.maple.server.common.constant;

/**
 * 权限相关的常量
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-09 20:07
 */
public class SecurityConst {
    /**
     * 登陆的url
     */
    public static final String LOGIN_URL = "/login";

    /**
     * 用于标记是否踢出的标识key
     */
    public static final String KICK_OUT_ATTRIBUTE_KEY = "kick-out-sign";

    /**
     * 用户账号在session中的key
     */
    public static final String USER_ACCOUNT_SESSION_KEY = "user-account";
}
