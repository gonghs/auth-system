package com.maple.starter.generator;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.maple.starter.generator.properties.CodeGeneratorProperties;
import com.maple.starter.generator.properties.DbProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
    private final DbProperties dbProperties;
    private final CodeGeneratorProperties codeGeneratorProperties;


    /**
     * 生成代码
     */
    public void generator() {
        AutoGenerator generator = new AutoGenerator();
        DataSourceConfig dsc = new DataSourceConfig()
                .setTypeConvert(new MySqlTypeConvert() {
                    // 自定义数据库表字段类型转换【可选】
                    @Override
                    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                        log.info("转换类型: {}", fieldType);
                        // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
                        return super.processTypeConvert(globalConfig, fieldType);
                    }
                }).setDriverName(dbProperties.getDriverClassName())
                .setUrl(dbProperties.getUrl())
                .setUsername(dbProperties.getUsername())
                .setPassword(dbProperties.getPassword());

        generator.setCodeGeneratorProperties(codeGeneratorProperties);
        // 启用设置 并执行生成
        generator.setDataSource(dsc);
        generator.execute();
    }
}
