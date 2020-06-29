package com.maple.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.maple.server.common.constant.GlobalConst;
import lombok.Data;
import lombok.SneakyThrows;

import java.net.URLEncoder;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * 翻译工具  文档详见http://fanyi.youdao.com/openapi?path=data-mode
 *
 * @author maple
 * @version 1.0
 * @since 2020-06-17 14:33
 */
public class TranslateUtils {
    public static final String TRANSLATE_URL = "http://fanyi.youdao.com/openapi" +
            ".do?keyfrom=yangchong&key=520150590&type=data&doctype=json&version=1.1&q=";
    public static final String TRANSLATE_URL2 = "http://fanyi.youdao.com/translate?&doctype=json&type=AUTO&i=";

    public static Resp translate(String word) {
        return JSONUtil.toBean(HttpUtil.get(getUrl(word)), Resp.class);
    }

    @SneakyThrows
    public static String getUrl(String word) {
        return TRANSLATE_URL + URLEncoder.encode(word, GlobalConst.DEFAULT_CHARSET_STR);
    }

    @Data
    public static class Resp {
        /**
         * 有道翻译结果
         */
        private List<String> translation;
        /**
         * 基本词典翻译结果
         */
        private List<Basic> basic;
        /**
         * 查询词语
         */
        private List<String> query;
        /**
         * 错误码
         */
        private List<String> errorCode;
        /**
         * 网络释义
         */
        private List<Web> web;

        /**
         * 获取所有的翻译文本 忽略网络释义
         */
        public List<String> getAllTextIgnoreWeb() {
            List<String> allList = CollUtil.isEmpty(translation) ? CollUtil.newArrayList() : translation;
            if (CollUtil.isEmpty(basic)) {
                System.out.println(allList);
                return allList;
            }
            for (Basic item : basic) {
                CollUtil.addAll(allList, item.getExplains());
            }
            System.out.println(allList);
            return allList;
        }
        /**
         * 获取翻译文本
         */
        public Optional<String> getShortText() {
            return getAllTextIgnoreWeb().stream().map(item -> StrUtil.replace(item," ","_")).map(StrUtil::toCamelCase)
                    .sorted(Comparator.comparingInt(String::length))
                    .findAny();
        }
        @Data
        private static class Basic {
            private List<String> explains;
        }

        @Data
        private static class Web {
            private List<String> value;
            private String key;
        }
    }
}
