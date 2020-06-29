package com.maple.server.tool.column.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


/**
 * 字段配置工具
 *
 * @author maple
 * @desc 多租户配置
 * @date 2019/12/25
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "mp.column-tool")
public class ColumnToolProperties {

    /**
     * 维护租户列名称
     */
    private String column = "tenant_id";

    /**
     * 配置不进行多租户隔离的表名
     */
    private List<String> ignoreTables = new ArrayList<>();

    /**
     * 需要多租户隔离的表
     */
    private List<String> needTables = new ArrayList<>();

    /**
     * 启动状态
     */
    private Boolean enable;
}