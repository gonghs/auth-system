package com.maple.starter.generator.ext;

import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.maple.common.constant.SymbolConst;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字段类型拓展
 *
 * @author maple
 * @version 1.0
 * @since 2020-07-25 16:33
 */
@Getter
@AllArgsConstructor
public class ColumnTypeExt implements IColumnType {
    /**
     * 字段类型
     */
    private final String type;
    /**
     * 字段类型完整名
     */
    private String pkg;

    public void setPkg(String pkg) {
        this.pkg = pkg + SymbolConst.POINT + type;
    }
}
