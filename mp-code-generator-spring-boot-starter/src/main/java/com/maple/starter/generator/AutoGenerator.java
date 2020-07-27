package com.maple.starter.generator;

import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.maple.starter.generator.ext.FreemarkerTemplateEngineExt;
import com.maple.starter.generator.properties.CodeGeneratorProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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
        // 初始化配置
        if (null == config) {
            config = new ConfigBuilder(getPackageInfo(), getDataSource(), getStrategy(), getTemplate(),
                    getGlobalConfig());
            if (null != injectionConfig) {
                injectionConfig.setConfig(config);
            }
        }
        if (null == getTemplateEngine()) {
            // 默认使用freemarker扩展
            setTemplateEngine(new FreemarkerTemplateEngineExt().init(codeGeneratorProperties,enumConfig));
        }
        // 模板引擎初始化执行文件输出
        this.getTemplateEngine().init(this.pretreatmentConfigBuilder(config)).mkdirs().batchOutput().open();
        log.info("==========================文件生成完成！！！==========================");
    }



}
