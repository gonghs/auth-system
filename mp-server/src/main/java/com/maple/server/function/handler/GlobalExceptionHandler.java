package com.maple.server.function.handler;

import com.maple.server.common.builder.Results;
import com.maple.server.common.entity.Result;
import com.maple.server.common.exception.AuthException;
import com.maple.server.common.exception.ErrorCode;
import com.maple.server.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;


/**
 * 全局异常捕获器
 *
 * @author maple
 * @version 1.0
 * @since 2019-07-27 16:08
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @Autowired
    private HttpServletRequest request;

    /**
     * 服务异常拦截
     *
     * @param ex 异常
     * @return 异常结果对象
     */
    @ExceptionHandler(value = {ServiceException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> serviceException(ServiceException ex) {
        logThrowable(false, request, ex);
        return Results.failure(ex);
    }

    /**
     * shiro无权限异常
     *
     * @param ex 异常
     * @return 异常结果对象
     */
    @ExceptionHandler(value = {UnauthorizedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<Object> serviceException(UnauthorizedException ex) {
        logThrowable(false, request, ex);
        return Results.failure(ErrorCode.AUTH_ERROR.code(), ex.getMessage()).setDetail(ex.getMessage());
    }

    /**
     * 权限异常拦截
     *
     * @param ex 异常
     * @return 异常结果对象
     */
    @ExceptionHandler(value = {AuthException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<Object> authException(AuthException ex) {
        logThrowable(false, request, ex);
        return Results.failure(ErrorCode.AUTH_ERROR.code(), ex.getMessage()).setDetail(ex.getMessage());
    }

    /**
     * post RequestParam 参数校验异常拦截
     *
     * @param ex 异常
     * @return 异常结果对象
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<Object> constraintViolationException(ConstraintViolationException ex) {
        // 获取调试信息(具体校验失败字段)
        String detail = ex.getConstraintViolations().stream().map(item ->
                String.format("fieldName: %s,message: %s", item.getPropertyPath(), item.getMessage()))
                .collect(Collectors.joining(" | "));
        logThrowable(false, request, ex);
        return Results.failure(ErrorCode.VALIDATION_ERROR.code(), ErrorCode.VALIDATION_ERROR.message()).setDetail(detail);
    }

    /**
     * post requestBody 请求参数校验异常拦截
     *
     * @param ex 异常
     * @return 异常结果对象
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<Object> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        // 获取调试信息(具体校验失败字段)
        String detail = ex.getBindingResult().getFieldErrors().stream().map(item ->
                String.format("fieldName: %s,message: %s", item.getField(), item.getDefaultMessage()))
                .collect(Collectors.joining(" | "));
        logThrowable(false, request, ex);
        return Results.failure(ErrorCode.VALIDATION_ERROR.code(), ErrorCode.VALIDATION_ERROR.message()).setDetail(detail);
    }

    /**
     * get请求参数校验异常拦截
     *
     * @param ex 异常
     * @return 异常结果对象
     */
    @ExceptionHandler(value = {BindException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<Object> bindException(BindException ex) {
        // 获取调试信息(具体校验失败字段)
        String detail = ex.getFieldErrors().stream().map(item ->
                String.format("fieldName: %s,message: %s", item.getField(), item.getDefaultMessage()))
                .collect(Collectors.joining(" | "));
        logThrowable(false, request, ex);
        return Results.failure(ErrorCode.VALIDATION_ERROR.code(), ErrorCode.VALIDATION_ERROR.message()).setDetail(detail);
    }

    @ExceptionHandler(value = Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> defaultErrorHandler(Throwable throwable) {
        logThrowable(true, request, throwable);
        return Results.failure(ErrorCode.UNKNOWN_ERROR.code(), ErrorCode.UNKNOWN_ERROR.message()).setDetail(throwable.getMessage());
    }

    private void logThrowable(boolean error, HttpServletRequest request, Throwable throwable) {
        if (error) {
            log.error("[" + request.getMethod() + "] " + request.getRequestURI() +
                            (StringUtils.isEmpty(request.getQueryString()) ? "" : "?" + request.getQueryString()) + " ",
                    throwable);
        } else if (log.isInfoEnabled()) {
            log.info("[" + request.getMethod() + "] " + request.getRequestURI() +
                    (StringUtils.isEmpty(request.getQueryString()) ? "" : "?" + request.getQueryString()) + " " +
                    (throwable instanceof ServiceException ?
                            throwable.toString() :
                            (throwable.getClass().getName() + ":" + throwable.getMessage())));
        }
    }
}
