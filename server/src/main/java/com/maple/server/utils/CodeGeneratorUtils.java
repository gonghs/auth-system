package com.maple.server.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.maple.server.properties.DbProperties;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 代码生成器工具
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-09 00:19
 */
@Slf4j
@UtilityClass
public class CodeGeneratorUtils {
    /**
     * 生成代码
     *
     * @param dbProperties 数据库配置
     * @param tableName    生成的表名
     * @param modelName    模块名 dto.x service.x 等
     */
    public void generator(DbProperties dbProperties, String modelName, String... tableName) {
        modelName = StringUtils.isBlank(modelName) ? "" : modelName;
        AutoGenerator generator = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig()
                .setAuthor("maple")
                .setOutputDir(PathUtils.getCodePath())
                //开启swagger2文档
                .setSwagger2(true)
                // 是否覆盖文件
                //                .setFileOverride(true)
                //自定义文件命名，注意 % s 会自动填充表实体属性
                .setEntityName("%sDTO")
                .setMapperName("%sMapper")
                .setXmlName("%sDao")
                .setServiceName("%sService")
                .setServiceImplName("%sServiceImpl")
                .setControllerName("%sController");
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
        StrategyConfig strategy = new StrategyConfig()
                .setNaming(NamingStrategy.underline_to_camel)
                .setEntityLombokModel(true)
                .setInclude(tableName).setSuperEntityClass("com.maple.dto.base.BaseDTO")
                .setSuperEntityColumns("id", "desc", "modify_time", "create_time", "data_status")
                // 自定义 mapper 父类
                .setSuperMapperClass("com.baomidou.mybatisplus.core.mapper.BaseMapper")
                // 自定义 service 父类
                .setSuperServiceClass("com.baomidou.mybatisplus.extension.service.IService")
                // 自定义 service 实现类父类
                .setSuperServiceImplClass("com.baomidou.mybatisplus.extension.service.impl.ServiceImpl")
                .setSuperControllerClass("com.maple.controller.BaseController");
        // 包配置
        PackageConfig packageConfig = new PackageConfig()
                .setEntity(joinModelName("dto", modelName))
                .setController(joinModelName("controller", modelName))
                .setMapper(joinModelName("dao", modelName))
                .setService(joinModelName("service", modelName))
                .setServiceImpl(joinModelName("service", modelName) + ".impl")
                .setParent("com.maple");

        // 模板配置 不生成xml
        TemplateConfig templateConfig = new TemplateConfig().setXml(null);

        // 启用设置 并执行生成
        generator.setGlobalConfig(gc).setDataSource(dsc).setStrategy(strategy).setPackageInfo(packageConfig)
                .setTemplateEngine(new FreemarkerTemplateEngine()).setTemplate(templateConfig);

        generator.execute();
    }

    /**
     * 拼接 模块名
     *
     * @param name      名称
     * @param modelName 模块名
     * @return 拼接后的名称
     */
    private String joinModelName(String name, String modelName) {
        return StringUtils.isBlank(modelName) ? name : name + "." + modelName;
    }
}
