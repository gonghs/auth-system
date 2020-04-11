package com.maple.starter.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.maple.starter.generator.properties.CodeGeneratorProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

/**
 * 代码生成器工具
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-09 00:19
 */
@Slf4j
@AllArgsConstructor
public class CodeGeneratorUtils {
    private final DataSourceProperties dbProperties;
    private final CodeGeneratorProperties codeGeneratorProperties;

    /**
     * 生成代码
     */
    public void generator() {
        AutoGenerator generator = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = codeGeneratorProperties.getGlobalConfig();
        //数据库配置
        DataSourceConfig dsc = new DataSourceConfig()
                .setTypeConvert(new MySqlTypeConvert() {
                    // 自定义数据库表字段类型转换【可选】
                    @Override
                    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                        log.info("转换类型: {}", fieldType);
                        // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
                        return super.processTypeConvert(globalConfig, fieldType);
                    }
                }).setDbType(DbType.MYSQL)
                .setDriverName(dbProperties.getDriverClassName())
                .setUrl(dbProperties.getUrl())
                .setUsername(dbProperties.getUsername())
                .setPassword(dbProperties.getPassword());

        // 策略配置
        StrategyConfig strategy =
                codeGeneratorProperties.getStrategyConfig().setInclude(codeGeneratorProperties.getGenTables());
        // 包配置
        PackageConfig packageConfig = codeGeneratorProperties.getPackageConfig();

        // 模板配置 不生成xml
        TemplateConfig templateConfig = new TemplateConfig().setXml(null);

        // 启用设置 并执行生成
        generator.setGlobalConfig(gc).setDataSource(dsc).setStrategy(strategy).setPackageInfo(packageConfig)
                .setTemplateEngine(new FreemarkerTemplateEngine()).setTemplate(templateConfig);

        generator.execute();
    }
}
