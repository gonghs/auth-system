package com.maple.server.config;

import com.maple.server.tool.column.handler.ColumnHandler;
import com.maple.server.tool.column.handler.ColumnSqlParser;
import com.maple.server.tool.column.interceptor.ColumnInterceptor;
import com.maple.server.tool.column.parse.ISqlParser;
import com.maple.server.tool.column.properties.ColumnToolHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * mybatis配置
 *
 * @author maple
 * @version 1.0
 * @since 2020-06-09 23:57
 */
@Configuration
public class MybatisConfig {
    /**
     * 创建租户维护处理器对象
     *
     * @return 处理后的租户维护处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public ColumnHandler columnHandler() {
        return new ColumnToolHandler();
    }


    @Bean
    public ColumnInterceptor columnInterceptor(ColumnHandler columnHandler){
        ColumnInterceptor columnInterceptor = new ColumnInterceptor();
        List<ISqlParser> sqlParserList = new ArrayList<>();
        ColumnSqlParser columnSqlParser = new ColumnSqlParser();
        columnSqlParser.setColumnHandler(columnHandler);
        sqlParserList.add(columnSqlParser);
        columnInterceptor.setSqlParserList(sqlParserList);
        return columnInterceptor;
    }
}
