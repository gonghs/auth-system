package com.maple.common.entity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 分页参数的请求对象 继承mybatis plus 取消swagger文档泛型参数的写入
 *
 * @author gonghs
 * @version 1.0
 * @since 2019-10-04 11:18
 */
public class ReqPage<T> extends Page<T> {
    @Override
    @ApiModelProperty(hidden = true)
    public List<T> getRecords() {
        return super.getRecords();
    }

    @Override
    @ApiModelProperty(hidden = true)
    public long getTotal() {
        return super.getTotal();
    }

    @Override
    @ApiModelProperty(example = "10")
    public long getSize() {
        return super.getSize();
    }

    @Override
    @ApiModelProperty(example = "0")
    public long getCurrent() {
        return super.getCurrent();
    }
}
