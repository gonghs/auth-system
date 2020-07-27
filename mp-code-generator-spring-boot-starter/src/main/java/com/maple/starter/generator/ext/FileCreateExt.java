package com.maple.starter.generator.ext;

import com.baomidou.mybatisplus.generator.config.IFileCreate;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.rules.FileType;

import java.io.File;
import java.util.Objects;

/**
 * 判断文件是否需要创建
 *
 * @author maple
 * @version 1.0
 * @since 2020-07-24 15:34
 */
public class FileCreateExt implements IFileCreate {
    @Override
    public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
        // 只创建自定义文件
        if (!Objects.equals(fileType, FileType.OTHER)) {
            return false;
        }
        // 全局判断【默认】
        File file = new File(filePath);
        boolean exist = file.exists();
        if (!exist) {
            file.getParentFile().mkdirs();
        }
        return !exist || configBuilder.getGlobalConfig().isFileOverride();
    }
}
