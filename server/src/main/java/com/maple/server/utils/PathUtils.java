package com.maple.server.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.net.URL;
import java.net.URLDecoder;
import java.util.Optional;

/**
 * 路径工具
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-09 00:39
 */
@UtilityClass
public class PathUtils {
    /**
     * 获取项目路径
     *
     * @return 项目路径
     */
    public String getProjectPath() {
        return System.getProperty("user.dir");
    }

    /**
     * 获取代码路径
     *
     * @return 代码路径
     */
    public String getCodePath() {
        return System.getProperty("user.dir") + "/src/main/java";
    }

    @SneakyThrows
    public String getResourcePath(String filePath) {
        return URLDecoder.decode(Optional.ofNullable(PathUtils.class.getClassLoader().getResource(filePath)).map(URL::getPath
        ).orElse(""), "UTF-8");
    }
}
