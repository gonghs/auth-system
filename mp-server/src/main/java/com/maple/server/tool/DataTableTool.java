package com.maple.server.tool;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.maple.server.dto.admin.DataTablesDTO;

/**
 * data table 工具
 *
 * @author maple
 * @version 1.0
 * @since 2019-10-31 22:04
 */
public class DataTableTool {
    public static <T> DataTablesDTO<T> convert(IPage<T> page) {
        return new DataTablesDTO<T>().setData(page.getRecords()).setRecordsTotal(page.getTotal()).setRecordsFiltered(page.getTotal());
    }
}
