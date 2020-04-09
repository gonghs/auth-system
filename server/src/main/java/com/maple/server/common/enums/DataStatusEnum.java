package com.maple.server.common.enums;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Getter;

/**
 * 数据状态枚举
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-15 16:19
 */
@Getter
@JSONType(deserializer = EnumDeserializer.class)
public enum DataStatusEnum implements BaseEnum {
    /**
     * 数据状态
     */
    NORMAL(1, "正常"), DELETE(0, "删除"),
    ;

    DataStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 值
     */
    private Integer value;

    /**
     * 描述
     */
    private String desc;

    @Override
    public String toString() {
        return getDesc();
    }
}