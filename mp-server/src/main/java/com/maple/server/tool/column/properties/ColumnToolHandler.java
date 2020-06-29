package com.maple.server.tool.column.properties;

import cn.hutool.core.util.StrUtil;
import com.maple.server.tool.column.handler.ColumnHandler;
import com.maple.server.tool.column.utils.ColumnContextHolder;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author Trazen
 * @desc
 * @date 2019/12/25
 */
@Slf4j
@EnableConfigurationProperties({ ColumnToolProperties.class})
public class ColumnToolHandler implements ColumnHandler {

    @Autowired
    private ColumnToolProperties properties;


    @Override
    public Expression getColumnValue(boolean where) {
        String columnValue = ColumnContextHolder.getColumnValue();
        log.debug("当前字段值为 >> {}", columnValue);

        if (columnValue == null) {
            return new LongValue(1);
        }
        return new StringValue(columnValue);
    }

    /**
     * 获取租户字段名
     *
     * @return 租户字段名
     */
    @Override
    public String getColumnName() {
        return properties.getColumn();
    }

    /**
     * 根据表名判断是否进行过滤
     *
     * @param tableName 表名
     * @return 是否进行过滤
     */
    @Override
    public boolean doTableFilter(String tableName) {
        String columnValue = ColumnContextHolder.getColumnValue();
        // 租户中ID 为空，查询全部，不进行过滤
        if (StrUtil.isEmpty(columnValue)) {
            return Boolean.FALSE;
        }
        return properties.getNeedTables().stream().anyMatch(
                (e) -> e.equalsIgnoreCase(tableName)
        );
    }
}
