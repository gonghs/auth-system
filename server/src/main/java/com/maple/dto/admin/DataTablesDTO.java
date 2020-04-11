package com.maple.dto.admin;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * dataTables 返回的DTO
 *
 * @author maple
 * @version 1.0
 * @since 2019-10-31 21:48
 */
@Data
@Accessors(chain = true)
public class DataTablesDTO<T> {
    /**
     * 绘画次数
     */
    private Long draw;
    /**
     * 最大数目
     */
    private Long recordsTotal;
    /**
     * 过滤数目
     */
    private Long recordsFiltered;
    /**
     * 数据列表
     */
    private List<T> data;
}
