package com.maple.starter.generator;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.maple.starter.generator.ext.FreemarkerTemplateEngineExt;
import com.maple.starter.generator.properties.CodeGeneratorProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 自定义自动生成器 用于重写并生成枚举类
 *
 * @author maple
 * @version 1.0
 * @since 2020-06-12 01:01
 */
@Slf4j
@Getter
@Setter
public class AutoGenerator extends com.baomidou.mybatisplus.generator.AutoGenerator {
    private CodeGeneratorProperties codeGeneratorProperties;
    private CodeGeneratorProperties.EnumConfig enumConfig;

    public void setCodeGeneratorProperties(CodeGeneratorProperties codeGeneratorProperties) {
        this.codeGeneratorProperties = codeGeneratorProperties;
        this.setPackageInfo(codeGeneratorProperties.getPackageConfig());
        this.setGlobalConfig(codeGeneratorProperties.getGlobalConfig());
        this.setStrategy(codeGeneratorProperties.getStrategyConfig().setInclude(codeGeneratorProperties.getGenTables()));
        this.setEnumConfig(codeGeneratorProperties.getEnumConfig());
        this.setTemplate(codeGeneratorProperties.getTemplateConfig());
    }

    @Override
    public void execute() {
        log.info("==========================准备生成文件...==========================");
        // 原有配置不支持指定不同目录 清空原有模板配置
        clearTemplate();
        createInjectConfig();
        // 初始化配置
        if (null == config) {
            config = new ConfigBuilder(getPackageInfo(), getDataSource(), getStrategy(), getTemplate(), getGlobalConfig());
            if (null != injectionConfig) {
                injectionConfig.setConfig(config);
            }
        }
        if (null == getTemplateEngine()) {
            // 默认使用freemarker扩展
            setTemplateEngine(new FreemarkerTemplateEngineExt().init(enumConfig));
        }
        // 模板引擎初始化执行文件输出
        this.getTemplateEngine().init(this.pretreatmentConfigBuilder(config)).mkdirs().batchOutput().open();
        log.info("==========================文件生成完成！！！==========================");
    }



    private String entity = "/templates/entity.java";
    private String entityKt = "/templates/entity.kt";
    private String service = "/templates/service.java";
    private String serviceImpl = "/templates/serviceImpl.java";
    private String mapper = "/templates/mapper.java";
    private String xml = "/templates/mapper.xml";
    private String controller = "/templates/controller.java";

    /**
     * 创建自定义配置替换原有模板配置
     */
    private void createInjectConfig() {
        if (Objects.isNull(this.injectionConfig)) {
            this.injectionConfig = new InjectionConfig() {
                @Override
                public void initMap() {
                }
            };
        }
        // 文件输出列表
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig(entity) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return codeGeneratorProperties.getPackageConfig().getEntityDir()
                        + codeGeneratorProperties.getBasePackage();
            }
        });

        if (CollUtil.isEmpty(this.injectionConfig.getFileOutConfigList())) {
            this.injectionConfig.setFileOutConfigList(focList);
            return;
        }

        this.injectionConfig.getFileOutConfigList().addAll(focList);
    }


    private void clearTemplate() {
        this.getTemplate().setXml(null).setEntity(null).setController(null).setMapper(null).setService(null).setServiceImpl(null);
    }
}
