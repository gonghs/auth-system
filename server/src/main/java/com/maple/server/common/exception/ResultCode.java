package com.maple.server.common.exception;

/**
 * 结果码接口
 *
 * @author maple
 * @version 1.0
 * @since 2019-07-27 15:58
 */
public interface ResultCode {
    /**
     * 获取响应码
     * @return 响应码
     */
    String code();

    /**
     * 获取响应码信息
     * @return 响应信息
     */
    String message();
}
