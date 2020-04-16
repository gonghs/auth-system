package com.maple.server.dto.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maple.server.common.enums.DataStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 基础DTO
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-07 11:08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BasePageDTO extends Page {

    private static final long serialVersionUID = -5180447363632554128L;
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "描述")
    @TableField(value = "`desc`")
    private String desc;

    @ApiModelProperty(value = "数据状态 0:删除 1:有效")
    private DataStatusEnum dataStatus;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;
}
