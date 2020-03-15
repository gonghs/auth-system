package com.maple.common.entity;

import com.maple.common.constant.GlobalConst;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;

/**
 * Result对象
 *
 * @author maple
 * @version 1.0
 * @since 2019-9-2
 */
@Data
@Accessors(chain = true)
@ApiResponses({
        @ApiResponse(code = 1, message = "成功"),
        @ApiResponse(code = 999, message = "用户无权限")
})
public class Result<T> implements Serializable {
    private static final long serialVersionUID = -4408341719434417427L;

    /**
     * 编码
     */
    private String code;

    /**
     * 消息，可能会显示给用户
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 用于调试的信息，可以带有非常详细的业务数据，便于排查错误。
     */
    private String detail;

    /**
     * 每次请求的Id 用于跟踪请求日志
     */
    private String requestId;

    public boolean isSuccess() {
        return Objects.equals(GlobalConst.SUCCESS_CODE, code);
    }

    public boolean isFail() {
        return !isSuccess();
    }
}
