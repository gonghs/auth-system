package com.maple.starter.generator.utils;


import org.yaml.snakeyaml.Yaml;

/**
 * yml解析工具
 *
 * @author maple
 * @version 1.0
 * @since 2020-06-28 17:33
 */
public class YmlUtils {

    private static final String FILENAME = "application.yml";

    /**
     * 获取yml文件中的值
     *
     * @param type 读取配置为指定类型
     */
    public static <T> T loadClass(Class<T> type) {
        Yaml yaml = new Yaml();
        return yaml.loadAs(YmlUtils.class.getClassLoader().getResourceAsStream(FILENAME), type);
    }
}
