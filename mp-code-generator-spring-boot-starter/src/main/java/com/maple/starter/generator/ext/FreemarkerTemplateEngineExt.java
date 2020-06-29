package com.maple.starter.generator.ext;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.maple.common.constant.SymbolConst;
import com.maple.common.utils.TranslateUtils;
import com.maple.starter.generator.properties.CodeGeneratorProperties;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * freemarker模板扩展
 *
 * @author maple
 * @version 1.0
 * @since 2020-06-19 18:07
 */
@Slf4j
public class FreemarkerTemplateEngineExt extends FreemarkerTemplateEngine {
    static Pattern commonPattern = Pattern.compile("([0-9]{1,2}:[^,]*?,)*?([0-9]{1,2}:[^,]*?)(?=;)");
    private CodeGeneratorProperties.EnumConfig enumConfig;

    /**
     * 初始化枚举配置
     *
     * @param enumConfig 枚举配置
     */
    public FreemarkerTemplateEngineExt init(CodeGeneratorProperties.EnumConfig enumConfig) {
        this.enumConfig = enumConfig;
        return this;
    }

    @Override
    public AbstractTemplateEngine batchOutput() {
        // 生成枚举
        genEnum();
        return super.batchOutput();
    }

    /**
     * 生成枚举类
     */
    protected void genEnum() {
        ConfigBuilder config = this.getConfigBuilder();
        List<TableInfo> allTableInfoList = config.getTableInfoList();
        // 注释满足格式[码值]:[描述],[码值]:[描述]; 则生成一个枚举类
        for (TableInfo tableInfo : allTableInfoList) {
            if (CollUtil.isEmpty(tableInfo.getFields())) {
                continue;
            }
            Map<String, Object> objectMap = this.getObjectMap(tableInfo);
            for (TableField field : tableInfo.getFields()) {
                Matcher matcher = commonPattern.matcher(field.getComment());
                if (!matcher.find()) {
                    continue;
                }
                // 搜集枚举数组
                List<Map<String, String>> enumList =
                        StrUtil.splitTrim(matcher.group(0), SymbolConst.COMMA).stream().filter(str ->
                                str.split(SymbolConst.COLON).length == 2).map(str ->
                        {
                            String[] valueAndDesc = str.split(SymbolConst.COLON);
                            return MapUtil.<String, String>builder().put("value", valueAndDesc[0]).put("desc",
                                    valueAndDesc[1]).put("name",
                                    getEnumName(field, valueAndDesc[0], valueAndDesc[1])).build();
                        }).collect(Collectors.toList());
                // TODO 完善目录
                try {
                    objectMap.put("field", field);
                    objectMap.put("enumList", enumList);
                    this.writer(objectMap
                            ,
                            this.templateFilePath(enumConfig.getTemplatePath())
                            ,
                            config.getGlobalConfig().getOutputDir() + File.separator + String.format(enumConfig.getEnumName(),
                                    field.getCapitalName())
                                    + (config.getGlobalConfig().isKotlin() ? ".kt" : ".java"));
                } catch (Exception e) {
                    log.error("生成枚举错误!!!", e);
                }
            }

            //            String.format((String) pathInfo.get("mapper_path") + File.separator +
            //            codeGeneratorProperties.getGlobalConfig().getEnumName()
            //                    + config.getGlobalConfig().isKotlin() ? ".kt" : ".java", item.getCapitalName());
        }
    }

    /**
     * 获取枚举名 默认根据desc翻译获得 如果无翻译结果则返回字段名和value拼接的结果
     *
     * @param tableField 字段信息
     * @param value      值
     * @param desc       描述
     * @return 最简枚举名大写下划线形式
     */
    private String getEnumName(TableField tableField, String value, String desc) {
        return StrUtil.toUnderlineCase(TranslateUtils.translate(desc).getShortText()
                .orElse(tableField.getCapitalName() + value)).toUpperCase();
    }
}
