package com.maple.dto.base;

import com.baomidou.mybatisplus.annotation.*;
import com.maple.common.enums.DataStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础DTO
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-07 11:08
 */
@Data
public class BaseDTO implements Serializable {

    private static final long serialVersionUID = 3607422483228078665L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id", example = "111111")
    private Long id;

    @ApiModelProperty(value = "描述", example = "描述")
    @TableField(value = "`desc`")
    private String desc;

    @ApiModelProperty(value = "数据状态 0:删除 1:有效", example = "删除")
    @TableLogic
    private DataStatusEnum dataStatus;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间", example = "2019-01-01", hidden = true)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改时间", example = "2019-01-01", hidden = true)
    private Date modifyTime;
}
