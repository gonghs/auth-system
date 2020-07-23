package com.maple.common.builder;

import com.maple.common.context.RequestContext;
import com.maple.common.entity.Result;
import com.maple.common.exception.ErrorCode;
import com.maple.common.exception.ServiceException;
import com.maple.server.common.constant.GlobalConst;

/**
 * Result工具类，用于返回Result对象
 *
 * @author maple
 * @version 1.0
 * @since 2019-07-27 15:53
 */
public final class Results {

    /**
     * 成功
     *
     * @return Result<Void>
     */
    public static Result<Void> success() {
        return new Result<Void>()
                .setCode(GlobalConst.SUCCESS_CODE)
                .setRequestId(RequestContext.getRequestId());
    }

    /**
     * 成功
     *
     * @param data 并设置data参数
     * @param <T>  data的泛型
     * @return Result<T>
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>()
                .setCode(GlobalConst.SUCCESS_CODE)
                .setData(data)
                .setMessage(GlobalConst.SUCCESS_MESSAGE)
                .setRequestId(RequestContext.getRequestId());
    }

    /**
     * 业务异常，code统一使用3
     *
     * @param serviceException 业务异常
     * @param <T>              类型
     * @return result对象
     */
    public static <T> Result<T> failure(ServiceException serviceException) {
        return new Result<T>().setCode(ErrorCode.SERVICE_ERROR.code())
                .setMessage(serviceException.getMessage())
                .setDetail(serviceException.getDetail())
                .setRequestId(RequestContext.getRequestId());
    }

    /**
     * 返回带异常信息的响应结果，可以自己明确的系统错误
     *
     * @param code    错误编号
     * @param message 错误信息
     * @param <T>     对应data字段的数据类型
     * @return result 对象
     */
    public static <T> Result<T> failure(String code, String message) {
        return new Result<T>()
                .setCode(code)
                .setMessage(message)
                .setRequestId(RequestContext.getRequestId());
    }

    /**
     * 返回带异常信息的响应结果，可以自己明确的系统错误
     *
     * @param code      错误编号
     * @param message   错误信息
     * @param requestId 请求id
     * @param <T>       对应data字段的数据类型
     * @return result 对象
     */
    public static <T> Result<T> failure(String code, String message, String requestId) {
        return new Result<T>()
                .setCode(code)
                .setMessage(message)
                .setRequestId(requestId);
    }
}
