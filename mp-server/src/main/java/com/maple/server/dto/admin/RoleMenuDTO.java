package com.maple.server.dto.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户角色关联dto
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-08 23:53
 */
@Data
@TableName("role_menu")
public class RoleMenuDTO {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id", example = "111111")
    private Long id;

    @ApiModelProperty(value = "角色id", example = "111111")
    private Long roleId;

    @ApiModelProperty(value = "菜单id", example = "111111")
    private Long menuId;
}
