package com.maple.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务系统业务逻辑相关异常
 *
 * @author maple
 * @version V1.0
 * @since 2019-7-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = -8667394300356618037L;
    private String detail;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, String detail) {
        super(message);
        this.detail = detail;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(String message, String detail, Throwable cause) {
        super(message, cause);
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "ServiceException{" +
                "message='" + getMessage() + "'," +
                "detail='" + getDetail() + "'" +
                '}';
    }
}

