package com.maple.server.dto.admin;

import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.maple.server.dto.base.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色dto
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-08 23:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("role")
public class RoleDTO extends BaseDTO {
    @ApiModelProperty(value = "角色名称", example = "管理员")
    @TableField(condition = SqlCondition.LIKE)
    private String roleName;
}
