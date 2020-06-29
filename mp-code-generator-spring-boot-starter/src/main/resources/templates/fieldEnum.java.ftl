package com.maple.server.common.enums;


import com.alibaba.fastjson.annotation.JSONType;


@JSONType(deserializer = EnumDeserializer.class)
public enum ${field.capitalName}Enum {
    /**
     * 枚举列表
     */
    <#list enumList as enum>
    <#if enum_has_next>
    ${enum.name}("${enum.value}","${enum.desc}"),
    <#else>
    ${enum.name}("${enum.value}","${enum.desc}");
    </#if>
    </#list>
    ${field.capitalName}(Integer value, String desc){

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