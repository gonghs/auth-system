package com.maple.common.enums;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Getter;

import java.util.Objects;

/**
 * 是否枚举
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-15 16:19
 */
@Getter
@JSONType(deserializer = EnumDeserializer.class)
public enum YesOrNoEnum implements BaseEnum {
    /**
     * 数据状态
     */
    YES(1, "是"), NO(0, "否"),
    ;

    YesOrNoEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static boolean isYes(Integer value) {
        return Objects.equals(value, YES.getValue());
    }

    public static boolean isNo(Integer value) {
        return Objects.equals(value, NO.getValue());
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