package com.maple.server.utils;

import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-20 22:42
 */
@UtilityClass
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    private Pattern pattern2 = Pattern.compile("[A-Z]");

    private Pattern pattern = Pattern.compile("_(\\w)");

    /**
     * 驼峰转下划线
     *
     * @param str 需要转化的字符串
     * @return 转化后的字符串
     */
    public String camel2_(String str) {
        Matcher m = pattern2.matcher(str);
        StringBuffer sb = new StringBuffer(str);
        if (m.find()) {
            sb = new StringBuffer();
            m.appendReplacement(sb, "_" + m.group(0).toLowerCase());
            m.appendTail(sb);
        } else {
            return sb.toString();
        }
        return camel2_(sb.toString());
    }

    /**
     * 下划线转驼峰
     *
     * @param str 需要转化的字符串
     * @return 转化后的字符串
     */
    public String camel(String str) {
        // 利用正则删除下划线，把下划线后一位改成大写
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer(str);
        if (matcher.find()) {
            sb = new StringBuffer();
            // 将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。
            // 正则之前的字符和被替换的字符
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
            // 把之后的也添加到StringBuffer对象里
            matcher.appendTail(sb);
        } else {
            return sb.toString();
        }
        return camel(sb.toString());
    }

}
