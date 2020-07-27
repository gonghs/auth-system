package ${package.Enum};

<#if lombokModel>
import lombok.AllArgsConstructor;
import lombok.Getter;
</#if>
<#list import as pkg>
import ${pkg};
</#list>

<#if lombokModel>
@Getter
@AllArgsConstructor
</#if>
<#if (deserializerType??) && deserializerType == 0 >
    <#if deserializerType == 0 && deserializerClassName?? && deserializerClassName?trim?length gt 0>
@JSONType(deserializer = ${deserializerClassName}.class)
    </#if>
    <#if deserializerType == 1 && deserializerClassName?? && deserializerClassName?trim?length gt 0>
@JsonDeserialize(using = ${deserializerClassName}.class)
    </#if>
</#if>
<#if (interface??) && interface?trim?length gt 0>
public enum ${field.customMap.enumCapitalName} implement ${interface} {
<#else>
public enum ${field.customMap.enumCapitalName} {
</#if>
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
<#if !lombokModel>
    ${field.customMap.enumCapitalName}(Integer value, String desc){

    }
</#if>
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