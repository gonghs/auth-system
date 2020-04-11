package com.maple.dto.admin;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.maple.dto.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author maple
 * @since 2019-09-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("menu")
@ApiModel(value = "MenuDTO对象", description = "MenuDTO对象")
public class MenuDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "菜单名称", example = "系统管理")
    private String menuName;

    @ApiModelProperty(value = "父菜单id 0为根菜单", example = "0")
    private Long pid;

    @ApiModelProperty(value = "菜单url", example = "/user")
    private String url;

    @ApiModelProperty(value = "是否叶子节点", example = "0")
    private Integer isLeaf;

    @ApiModelProperty(value = "图标样式名", example = "up")
    private String icon;

    @ApiModelProperty(value = "子菜单", example = "1,2")
    @TableField(exist = false)
    private List<MenuDTO> children;
}
