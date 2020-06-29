package com.maple.server.tool.column.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 字段上下文容器
 *
 * @author maple
 * @version 1.0
 * @since 2020-06-09 23:44
 */
public class ColumnContextHolder {
    private static final ThreadLocal<String> COLUMN_VALUE_TL = new ThreadLocal<>();

    public static String getColumnValue() {
        return COLUMN_VALUE_TL.get();
    }

    public static void setColumnValue(String columnValue) {
        if (StringUtils.isBlank(columnValue)) {
            return;
        }
        COLUMN_VALUE_TL.set(columnValue);
    }

    public static void remove() {
        COLUMN_VALUE_TL.remove();
    }
}
