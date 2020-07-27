package com.maple.starter.generator.ext;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.maple.common.constant.SymbolConst;
import com.maple.common.utils.TranslateUtils;
import com.maple.starter.generator.properties.CodeGeneratorProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;
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
    private CodeGeneratorProperties codeGeneratorProperties;
    private CodeGeneratorProperties.EnumConfig enumConfig;

    /**
     * 初始化枚举配置
     *
     * @param enumConfig 枚举配置
     */
    public FreemarkerTemplateEngineExt init(CodeGeneratorProperties codeGeneratorProperties,
                                            CodeGeneratorProperties.EnumConfig enumConfig) {
        this.enumConfig = enumConfig;
        this.codeGeneratorProperties = codeGeneratorProperties;
        return this;
    }

    @Override
    public Map<String, Object> getObjectMap(TableInfo tableInfo) {
        return genEnumAndInitFieldEnumInfo(tableInfo);
    }

    @Override
    public AbstractTemplateEngine batchOutput() {
        // 原有配置不支持指定不同目录 清空原有模板配置
        createInjectConfig();
        return super.batchOutput();
    }

    /**
     * 生成枚举类并向ObjectMap初始化字段枚举相关数据
     *
     * @return objectMap 生成参数
     */
    protected Map<String, Object> genEnumAndInitFieldEnumInfo(TableInfo tableInfo) {
        Map<String, Object> objectMap = super.getObjectMap(tableInfo);
        if (!enumConfig.isEnabled()) {
            return objectMap;
        }
        // 注释满足格式[码值]:[描述],[码值]:[描述]; 则生成一个枚举类
        if (CollUtil.isEmpty(tableInfo.getFields())) {
            return objectMap;
        }
        for (TableField field : tableInfo.getFields()) {
            Matcher matcher = commonPattern.matcher(field.getComment());
            if (!matcher.find()) {
                continue;
            }
            // 搜集枚举数组
            List<Map<String, String>> enumList =
                    StrUtil.splitTrim(matcher.group(0), SymbolConst.COMMA).stream().filter(str ->
                            str.split(SymbolConst.COLON).length == this.enumConfig.getFields().size()).map(str ->
                    {
                        String[] valueAndDesc = str.split(SymbolConst.COLON);
                        return MapUtil.<String, String>builder().put("value", valueAndDesc[0]).put("desc",
                                valueAndDesc[1]).put("name",
                                getEnumName(field, valueAndDesc[0], valueAndDesc[1])).build();
                    }).collect(Collectors.toList());
            try {
                String enumCapitalName = String.format(this.enumConfig.getEnumName(), field.getCapitalName());
                // 枚举文件配置
                FileOutConfigExt enumConfig = getFileConfigAndInitPkg(this.enumConfig.getEnumDir()
                        , this.enumConfig.getEnumPkgName()
                        , tableI -> enumCapitalName, this.enumConfig.getTemplatePath()
                        , "Enum");
                field.setColumnType(new ColumnTypeExt(enumCapitalName, enumConfig.getImportPkgWithFileName(tableInfo)));
                // 枚举字段信息 用于实体生成 导入依赖和更改字段类型
                Map<String, Object> enumFieldMap = MapUtil.<String, Object>builder()
                        .put("enumPkg", field.getColumnType().getPkg())
                        .put("enumCapitalName", enumCapitalName)
                        .build();

                if (CollUtil.isEmpty(field.getCustomMap())) {
                    field.setCustomMap(enumFieldMap);
                } else {
                    field.getCustomMap().putAll(enumFieldMap);
                }
                // 为了防止配置覆盖 手动生成这部分 不放入配置列表
                MapBuilder<String, Object> enumObjectMap = MapUtil.<String, Object>builder()
                        .put("author", objectMap.get("author"))
                        .put("date", DateUtil.format(new Date(),"yyyy-MM-dd"))
                        .put("package", objectMap.get("package"))
                        // 搜集需要导入的包名
                        .put("import", getImportPkg(enumConfig))
                        .put("field", field)
                        .put("enumList", enumList)
                        .put("lombokModel", this.enumConfig.isLombokModel())
                        .put("deserializerType", this.enumConfig.getDeserializerType().ordinal())
                        .put("deserializerClassName", getClassName(this.enumConfig.getDeserializerClass()))
                        .put("interface", getClassName(this.enumConfig.getImplementInterface()));

                if (isCreate(FileType.OTHER, enumConfig.outputFile(tableInfo))) {
                    writer(enumObjectMap.build(), enumConfig.getTemplatePath(), enumConfig.outputFile(tableInfo));
                }
            } catch (Exception e) {
                log.error("生成枚举错误!!!", e);
            }
        }
        return objectMap;
    }

    private List<String> getImportPkg(FileOutConfigExt enumConfig) {
        List<String> importPkgList = CollUtil.newArrayList();
        importPkgList.add(StrUtil.replace(this.enumConfig.getImplementInterface(),"%s",""));
        importPkgList.add(this.enumConfig.getDeserializerClass());
        if (StrUtil.isBlank(this.enumConfig.getDeserializerClass())) {
            return importPkgList.stream().filter(StrUtil::isNotBlank).collect(Collectors.toList());
        }
        switch (this.enumConfig.getDeserializerType()) {
            case FAST_JSON:
                importPkgList.add("com.alibaba.fastjson.annotation.JSONType");
                break;
            case JACKSON:
                importPkgList.add("com.fasterxml.jackson.databind.annotation.JsonDeserialize");
                break;
            default:
                break;
        }
        return importPkgList.stream().filter(StrUtil::isNotBlank).collect(Collectors.toList());
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

    /**
     * 创建自定义配置替换原有模板配置
     */
    private void createInjectConfig() {
        // 修改包信息 加入模块
        getConfigBuilder().getPackageInfo().put(ConstVal.MODULE_NAME, codeGeneratorProperties.getModelName());
        // 默认文件输出列表
        List<com.baomidou.mybatisplus.generator.config.FileOutConfig> focList = new ArrayList<>();
        focList.add(getFileConfigAndInitPkg(codeGeneratorProperties.getPackageConfig().getEntityDir()
                , codeGeneratorProperties.getPackageConfig().getEntity()
                , TableInfo::getEntityName
                , ConstVal.TEMPLATE_ENTITY_JAVA, ConstVal.ENTITY));
        focList.add(getFileConfigAndInitPkg(codeGeneratorProperties.getPackageConfig().getMapperDir()
                , codeGeneratorProperties.getPackageConfig().getMapper()
                , TableInfo::getMapperName
                , ConstVal.TEMPLATE_MAPPER, ConstVal.MAPPER));
        focList.add(getFileConfigAndInitPkg(codeGeneratorProperties.getPackageConfig().getServiceDir()
                , codeGeneratorProperties.getPackageConfig().getService()
                , TableInfo::getServiceName
                , ConstVal.TEMPLATE_SERVICE, ConstVal.SERVICE));
        focList.add(getFileConfigAndInitPkg(codeGeneratorProperties.getPackageConfig().getServiceImplDir()
                , codeGeneratorProperties.getPackageConfig().getServiceImpl()
                , TableInfo::getServiceImplName
                , ConstVal.TEMPLATE_SERVICE_IMPL, ConstVal.SERVICE_IMPL));
        focList.add(getFileConfigAndInitPkg(codeGeneratorProperties.getPackageConfig().getControllerDir()
                , codeGeneratorProperties.getPackageConfig().getController()
                , TableInfo::getControllerName
                , ConstVal.TEMPLATE_CONTROLLER, ConstVal.CONTROLLER));

        addFileOutConfigList(focList);
    }

    private FileOutConfigExt getFileConfigAndInitPkg(String dir, String subPkg
            , Function<TableInfo, String> fileNameGetter, String templatePath, String pkgKey) {
        FileOutConfigExt cfg = new FileOutConfigExt(codeGeneratorProperties)
                .setDir(dir)
                .setSubPkg(subPkg)
                .setFileNameGetter(fileNameGetter);
        cfg.setTemplatePath(templateFilePath(templatePath));
        codeGeneratorProperties.getPackageConfig().getCustomPath().forEach((k, v) -> {
            if (StrUtil.isBlank(v) || !StrUtil.equalsIgnoreCase(pkgKey, k)) {
                return;
            }
            cfg.setCustomPath(v);
        });
        // 修改包参数
        getConfigBuilder().getPackageInfo().put(pkgKey, cfg.getImportPkg());
        return cfg;
    }

    private InjectionConfig getInjectionConfig() {
        if (Objects.isNull(getConfigBuilder().getInjectionConfig())) {
            getConfigBuilder().setInjectionConfig(new InjectionConfigExt());
        }
        if (Objects.isNull(getConfigBuilder().getInjectionConfig().getFileCreate())) {
            // 默认不输出旧配置 文件 已经用新配置替代
            getConfigBuilder().getInjectionConfig().setFileCreate(new FileCreateExt());
        }
        if (Objects.isNull(getConfigBuilder().getInjectionConfig().getMap())) {
            // 默认不输出旧配置 文件 已经用新配置替代
            getConfigBuilder().getInjectionConfig().setMap(MapUtil.newHashMap());
        }
        return getConfigBuilder().getInjectionConfig();
    }

    private void addFileOutConfigList(List<FileOutConfig> focList) {
        InjectionConfig injectionConfig = getInjectionConfig();

        if (CollUtil.isEmpty(getConfigBuilder().getInjectionConfig().getFileOutConfigList())) {
            injectionConfig.setFileOutConfigList(focList);
            return;
        }

        injectionConfig.getFileOutConfigList().addAll(focList);
    }

    /**
     * 从完整类名获取类名
     *
     * @param fullClassName 完整类名
     * @return 类名
     */
    private String getClassName(String fullClassName) {
        return StrUtil.subAfter(fullClassName, SymbolConst.POINT, true);
    }
}
