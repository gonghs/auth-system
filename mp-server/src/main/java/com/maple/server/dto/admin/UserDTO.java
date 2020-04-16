package com.maple.server.dto.admin;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.maple.server.dto.base.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 用户DTO
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-07 11:07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user")
public class UserDTO extends BaseDTO {

    @ApiModelProperty(value = "账号", example = "maple")
    @NotNull(groups = Login.class)
    private String account;

    @ApiModelProperty(value = "密码", example = "1")
    @NotNull(groups = Login.class)
    @JSONField(serialize = false)
    private String password;

    @ApiModelProperty(value = "用户名", example = "maple")
    private String userName;

    @ApiModelProperty(value = "昵称", example = "maple")
    private String nickName;

    @ApiModelProperty(value = "电话", example = "18011111111")
    private String phone;

    public interface Login {
    }
}
