package com.maple.server.common.exception;

/**
 * 异常码
 *
 * @author maple
 * @version 1.0
 * @since 2019-07-27 16:01
 */
public enum ErrorCode implements ResultCode {
    // 未知的系统错误
    UNKNOWN_ERROR("1", "未知错误"),
    VALIDATION_ERROR("2", "参数错误"),
    SERVICE_ERROR("3", "服务异常"),
    AUTH_ERROR("999","权限异常"),
    ;

    /**
     * 响应码
     */
    private final String code;

    /**
     * 响应消息
     */
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
