package com.maple.starter.generator.ext;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
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
import com.maple.starter.generator.constant.TemplateType;
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
    static Pattern commonPattern = Pattern.compile("#E\\{.*?}");
    private static final String COMMENT_BEGIN = "#E{";
    private static final String COMMENT_END = "}";
    private static final String ENUM_NAME_BEGIN = "(";
    private static final String ENUM_NAME_END = ")";

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
        return genCustomTempAndInitFieldEnumInfo(tableInfo);
    }

    @Override
    public AbstractTemplateEngine batchOutput() {
        // 原有配置不支持指定不同目录 清空原有模板配置
        createInjectConfig();
        return super.batchOutput();
    }

    /**
     * 生成枚举类并向ObjectMap初始化字段枚举相关数据
     * 注释满足格式#E{} 则生成一个枚举类 默认枚举名自动翻译 内容为[码值](枚举名):[描述]:[字段]...,[码值]:[描述]:[字段]...
     *
     * @return objectMap 生成参数
     */
    protected Map<String, Object> genCustomTempAndInitFieldEnumInfo(TableInfo tableInfo) {
        Map<String, Object> objectMap = super.getObjectMap(tableInfo);
        if (!enumConfig.isEnabled() || StrUtil.isBlank(enumConfig.getTemplatePath())) {
            return objectMap;
        }
        if (CollUtil.isEmpty(tableInfo.getFields())) {
            return objectMap;
        }
        for (TableField field : tableInfo.getFields()) {
            // 注释满足格式#E{} 则生成一个枚举类 默认枚举名自动翻译 内容为[码值](枚举名):[描述]:[字段]...,[码值]:[描述]:[字段]...
            Matcher matcher = commonPattern.matcher(field.getComment());
            if (!matcher.find()) {
                continue;
            }

            // 搜集枚举数组
            List<Map<String, Object>> enumList = getEnumListByComment(field
                    , StrUtil.subBetween(matcher.group(0), COMMENT_BEGIN, COMMENT_END));

            try {
                String enumCapitalName = String.format(this.enumConfig.getEnumName(), field.getCapitalName());
                // 枚举文件配置
                FileOutConfigExt enumConfig = getFileConfigAndInitPkg(this.enumConfig.getEnumDir()
                        , this.enumConfig.getEnumPkgName()
                        , tableI -> enumCapitalName, this.enumConfig.getTemplatePath()
                        , "Enum");
                Objects.requireNonNull(enumConfig);
                fillFieldEnumInfo(tableInfo, field, enumConfig);
                ;
                Map<String, Object> enumObjectMap = getEnumObjectMap(objectMap, enumList, field);
                createAndWriteFile(enumObjectMap, enumConfig, tableInfo);
            } catch (Exception e) {
                log.error("生成枚举错误!!!", e);
            }
        }
        genCustomTemplate(tableInfo, objectMap);
        return objectMap;
    }

    private List<Map<String, Object>> getEnumListByComment(TableField field, String comment) {
        // 根据类型排序 保证有code则code类型在首位 未定义默认值的数据
        List<EnumField> enumFieldList =
                this.enumConfig.getFields().stream()
                        .filter(item -> StrUtil.isBlank(item.getValue())).collect(Collectors.toList());
        // 定义了默认值的数组
        List<EnumField> defaultValueEnumFieldList =
                this.enumConfig.getFields().stream()
                        .filter(item -> StrUtil.isNotBlank(item.getValue())).collect(Collectors.toList());
        return StrUtil.splitTrim(comment, SymbolConst.COMMA).stream().filter(str ->
                str.split(SymbolConst.COLON).length == enumFieldList.size()).map(str ->
        {
            // 深克隆一份 保证不被覆盖
            List<EnumField> enumFields = ObjectUtil.cloneByStream(enumFieldList);
            String[] codeAndParamArr = str.split(SymbolConst.COLON);
            String name = "";
            for (int i = 0; i < codeAndParamArr.length; i++) {
                String codeAndParam = codeAndParamArr[i];
                // 若第一个序列包含()则使用()中的内容做为名称
                if (i == 0 && (StrUtil.isNotBlank(name = StrUtil.subBetween(codeAndParam, ENUM_NAME_BEGIN,
                        ENUM_NAME_END)))) {
                    enumFields.get(i).setValue(StrUtil.subBefore(codeAndParam, ENUM_NAME_BEGIN, false));
                    continue;
                }

                enumFields.get(i).setValue(codeAndParam);
            }
            // 参数
            return MapUtil.<String, Object>builder().put("params"
                    , CollUtil.addAll(enumFields, defaultValueEnumFieldList))
                    // 枚举名 默认值为
                    .put("name", StrUtil.isBlank(name) ? getEnumName(field, codeAndParamArr[0],
                            codeAndParamArr[1]) : StrUtil.toUnderlineCase(name).toUpperCase()).build();
        }).collect(Collectors.toList());
    }

    /**
     * 组装枚举生成数据
     *
     * @param objectMap 原objectMap
     * @param enumList  枚举列表
     * @param field     字段信息
     * @return 枚举生成数据
     */
    private Map<String, Object> getEnumObjectMap(Map<String, Object> objectMap, List<Map<String, Object>> enumList,
                                                 TableField field) {
        return MapUtil.<String, Object>builder()
                .put("author", objectMap.get("author"))
                .put("date", DateUtil.format(new Date(), "yyyy-MM-dd"))
                .put("package", objectMap.get("package"))
                .put("enumFieldList", this.enumConfig.getFields())
                // 搜集需要导入的包名
                .put("import", getImportPkg(this.enumConfig.getFields()))
                .put("field", field)
                .put("enumList", enumList)
                .put("lombokModel", this.enumConfig.isLombokModel())
                .put("deserializerType", this.enumConfig.getDeserializerType().ordinal())
                // 将接口占位符替换为 code类型字段的类名
                .put("deserializerClassName",
                        getClassName(this.enumConfig.getDeserializerClass()))
                .put("interface", getClassName(StrUtil.replace(this.enumConfig.getImplementInterface(), "%s",
                        this.enumConfig.getFields().stream().filter(item -> Objects.equals(item.getType(),
                                EnumField.EnumFieldTypeEnum.CODE)).findFirst().map(EnumField::getClassName).orElse(
                                "Object")))).build();
    }

    /**
     * 填充枚举字段信息
     *
     * @param tableInfo  表信息
     * @param field      字段信息
     * @param enumConfig 枚举文件配置
     */
    private void fillFieldEnumInfo(TableInfo tableInfo, TableField field, FileOutConfigExt enumConfig) {
        String enumCapitalName = String.format(this.enumConfig.getEnumName(), field.getCapitalName());
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
    }

    /**
     * 生成自定义模板
     *
     * @param tableInfo 表信息
     * @param objectMap 对象Map
     */
    private void genCustomTemplate(TableInfo tableInfo, Map<String, Object> objectMap) {
        if (Objects.isNull(codeGeneratorProperties.getTemplateConfig().getCustomTemplates())) {
            return;
        }
        boolean convert = tableInfo.isConvert();
        // 渲染自定义模板
        for (CustomTemplate customTemplate : codeGeneratorProperties.getTemplateConfig().getCustomTemplates()) {
            FileOutConfigExt fileOutConfigExt = getByType(customTemplate.getTemplateType(), customTemplate.getName());
            if (Objects.isNull(fileOutConfigExt) || StrUtil.isBlank(customTemplate.getClassName())) {
                continue;
            }
            String className = StrUtil.upperFirst(String.format(customTemplate.getClassName(), tableInfo.getName()));
            fileOutConfigExt.setFileNameGetter(item -> className);
            objectMap.put("entity", className);
            tableInfo.setConvert(false);
            // 修改包参数
            String oldPkgStr =
                    getConfigBuilder().getPackageInfo().get(customTemplate.getTemplateType().getTemplateKey());
            getConfigBuilder().getPackageInfo().put(customTemplate.getTemplateType().getTemplateKey(),
                    fileOutConfigExt.getImportPkg());
            createAndWriteFile(objectMap, fileOutConfigExt, tableInfo);
            getConfigBuilder().getPackageInfo().put(customTemplate.getTemplateType().getTemplateKey(), oldPkgStr);
        }
        // 还原配置
        tableInfo.setConvert(convert);
        objectMap.put("entity", tableInfo.getEntityName());
    }

    private List<String> getImportPkg(Collection<EnumField> enumFieldList) {
        List<String> importPkgList = CollUtil.newArrayList();
        importPkgList.add(StrUtil.replace(this.enumConfig.getImplementInterface(), "<%s>", ""));
        importPkgList.add(this.enumConfig.getDeserializerClass());
        // 枚举字段相关导入包
        List<String> clazzList =
                enumFieldList.stream().map(EnumField::getClazz).collect(Collectors.toList());
        for (String clazz : clazzList) {
            // java.lang包不需要导入
            if (StrUtil.startWith(clazz, "java.lang")) {
                continue;
            }
            importPkgList.add(StrUtil.subBefore(clazz, SymbolConst.POINT, true));
        }
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

        for (TemplateType templateType : TemplateType.values()) {
            FileOutConfigExt fileOutConfigExt = getByType(templateType);
            if (Objects.isNull(fileOutConfigExt)) {
                continue;
            }
            focList.add(fileOutConfigExt);
        }
        addFileOutConfigList(focList);
    }

    private FileOutConfigExt getByType(TemplateType templateType) {
        return getByType(templateType, null);
    }

    private FileOutConfigExt getByType(TemplateType templateType, String customName) {
        Objects.requireNonNull(templateType);
        switch (templateType) {
            case ENTITY:
                return getFileConfigAndInitPkg(codeGeneratorProperties.getPackageConfig().getEntityDir()
                        , codeGeneratorProperties.getPackageConfig().getEntity()
                        , TableInfo::getEntityName
                        , codeGeneratorProperties.getTemplateConfig().getEntity(false)
                        , StrUtil.blankToDefault(customName, ConstVal.ENTITY));
            case MAPPER:
                return getFileConfigAndInitPkg(codeGeneratorProperties.getPackageConfig().getMapperDir()
                        , codeGeneratorProperties.getPackageConfig().getMapper()
                        , TableInfo::getMapperName
                        , codeGeneratorProperties.getTemplateConfig().getMapper()
                        , StrUtil.blankToDefault(customName, ConstVal.MAPPER));
            case SERVICE:
                return getFileConfigAndInitPkg(codeGeneratorProperties.getPackageConfig().getServiceDir()
                        , codeGeneratorProperties.getPackageConfig().getService()
                        , TableInfo::getServiceName
                        , codeGeneratorProperties.getTemplateConfig().getService()
                        , StrUtil.blankToDefault(customName, ConstVal.SERVICE));
            case SERVICE_IMPL:
                return getFileConfigAndInitPkg(codeGeneratorProperties.getPackageConfig().getServiceImplDir()
                        , codeGeneratorProperties.getPackageConfig().getServiceImpl()
                        , TableInfo::getServiceImplName
                        , codeGeneratorProperties.getTemplateConfig().getServiceImpl()
                        , StrUtil.blankToDefault(customName, ConstVal.SERVICE_IMPL));
            case CONTROLLER:
                return getFileConfigAndInitPkg(codeGeneratorProperties.getPackageConfig().getControllerDir()
                        , codeGeneratorProperties.getPackageConfig().getController()
                        , TableInfo::getControllerName
                        , codeGeneratorProperties.getTemplateConfig().getController()
                        , StrUtil.blankToDefault(customName, ConstVal.CONTROLLER));
            default:
                return null;
        }
    }

    private FileOutConfigExt getFileConfigAndInitPkg(String dir, String subPkg
            , Function<TableInfo, String> fileNameGetter, String templatePath, String pkgKey) {
        // 模板路径被清空则不再生成
        if (StrUtil.isBlank(templatePath)) {
            return null;
        }
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

    private void createAndWriteFile(Map<String, Object> objectMap, FileOutConfigExt fileOutConfigExt,
                                    TableInfo tableInfo) {
        String filePath = fileOutConfigExt.outputFile(tableInfo);
        if (isCreate(FileType.OTHER, filePath)) {
            try {
                writer(objectMap, fileOutConfigExt.getTemplatePath(), filePath);
            } catch (Exception e) {
                log.error("文件生成有误", e);
            }
        }
    }
}
