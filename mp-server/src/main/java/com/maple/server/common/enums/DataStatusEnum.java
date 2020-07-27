package com.maple.server.common.enums;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据状态枚举
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-15 16:19
 */
@Getter
@AllArgsConstructor
@JSONType(deserializer = EnumDeserializer.class)
public enum DataStatusEnum implements BaseEnum {
    /**
     * 数据状态
     */
    NORMAL(1, "正常"), DELETE(0, "删除"),
    ;

    /**
     * 值
     */
    private final Integer value;

    /**
     * 描述
     */
    private final String desc;

    @Override
    public String toString() {
        return getDesc();
    }
}