package com.maple.starter.generator.constant;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.config.ConstVal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 路径常量
 *
 * @author maple
 * @version 1.0
 * @since 2020-07-24 15:56
 */
public class PathStructConst {
    public static final String DIR = "{dir}";
    public static final String PKG = "{pkg}";
    public static final String MOD = "{mod}";
    public static final String SUB_PKG = "{subPkg}";
    public static final String GLOBAL = "{global}";
    public static final String DEFAULT = StrUtil.join(File.separator, DIR, PKG, SUB_PKG, MOD);
    public static final Pattern MATCH_STRUCT = Pattern.compile("dir|pkg|mod|subPkg");

    public static List<String> getList(String pathStructStr) {
        Matcher matcher = MATCH_STRUCT.matcher(pathStructStr);
        List<String> rsList = new ArrayList<>();
        while (matcher.find()) {
            rsList.add(matcher.group(0));
        }
        return rsList;
    }

    /**
     * 连接路径字符串
     *
     * @param parentDir   路径常量字符串
     * @param packageName 包名
     * @return 连接后的路径
     */
    public static String joinPath(String parentDir, String packageName) {
        if (StringUtils.isEmpty(parentDir)) {
            parentDir = System.getProperty(ConstVal.JAVA_TMPDIR);
        }
        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir += File.separator;
        }
        packageName = packageName.replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
        return parentDir + packageName;
    }
}
