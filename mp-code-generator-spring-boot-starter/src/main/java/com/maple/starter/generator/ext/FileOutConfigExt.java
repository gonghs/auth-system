package com.maple.starter.generator.ext;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.maple.common.constant.SymbolConst;
import com.maple.starter.generator.constant.PathStructConst;
import com.maple.starter.generator.properties.CodeGeneratorProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.File;
import java.util.Map;
import java.util.function.Function;

/**
 * 文件输出配置
 *
 * @author maple
 * @version 1.0
 * @since 2020-07-24 16:48
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class FileOutConfigExt extends com.baomidou.mybatisplus.generator.config.FileOutConfig {
    private String dir;
    private String pkg;
    private String subPkg;
    private String mod;
    private String customPath;
    private Function<TableInfo, String> fileNameGetter;

    private final CodeGeneratorProperties codeGeneratorProperties;

    public FileOutConfigExt(CodeGeneratorProperties codeGeneratorProperties) {
        this.pkg = codeGeneratorProperties.getBasePackage();
        this.mod = codeGeneratorProperties.getModelName();
        this.codeGeneratorProperties = codeGeneratorProperties;
    }

    @Override
    public String outputFile(TableInfo tableInfo) {
        return PathStructConst.joinPath(codeGeneratorProperties.getGlobalConfig().getOutputDir(), getFilePath()) + File.separator + getFileName(tableInfo)  + suffixJavaOrKt();
    }

    /**
     * 获取文件路径
     */
    public String getFilePath() {
        return joinPackage(
                MapUtil.builder(PathStructConst.DIR, StrUtil.replace(dir, "%s", mod))
                        .put(PathStructConst.PKG, pkg)
                        .put(PathStructConst.SUB_PKG, subPkg)
                        .put(PathStructConst.MOD, mod)
                        .build()
        );
    }

    /**
     * 获取包路径
     */
    public String getImportPkg() {
        return joinPackage(
                MapUtil.builder(PathStructConst.DIR, "")
                        .put(PathStructConst.PKG, pkg)
                        .put(PathStructConst.SUB_PKG, subPkg)
                        .put(PathStructConst.MOD, mod)
                        .build()
        );
    }

    /**
     * 获取带文件名的包路径
     */
    public String getImportPkgWithFileName(TableInfo tableInfo) {
        return getImportPkg() + SymbolConst.POINT + getFileName(tableInfo);
    }

    public String getFileName(TableInfo tableInfo) {
        return fileNameGetter.apply(tableInfo);
    }

    /**
     * 连接包名
     *
     * @param pkgInfo 路径明细
     * @return 连接后的包名
     */
    private String joinPackage(Map<String, String> pkgInfo) {
        // 有自定义路径则取自定义路径 否则取路径规划
        String pathStruct = StrUtil.isBlank(customPath)
                ? codeGeneratorProperties.getPathStruct()
                : StrUtil.replace(customPath
                , PathStructConst.GLOBAL
                , codeGeneratorProperties.getPathStruct());
        pathStruct = StrUtil.replace(pathStruct, File.separator, SymbolConst.POINT);
        pathStruct = StrUtil.replace(pathStruct, SymbolConst.DIAGONAL, SymbolConst.POINT);
        StringBuilder pkgNameSb = new StringBuilder(pathStruct);
        pkgInfo.forEach((k, v) -> {
            String val = StrUtil.emptyIfNull(v);
            pkgNameSb.replace(0, pkgNameSb.length(), StrUtil.replace(pkgNameSb, k, val));
        });
        // 去除多余的点
        return StrUtil.join(SymbolConst.POINT, StrUtil.splitTrim(pkgNameSb, SymbolConst.POINT));
    }


    /**
     * 文件后缀
     */
    protected String suffixJavaOrKt() {
        return codeGeneratorProperties.getGlobalConfig().isKotlin() ? ConstVal.KT_SUFFIX : ConstVal.JAVA_SUFFIX;
    }
}
