package ${package.Enum};

<#if lombokModel>
import lombok.AllArgsConstructor;
import lombok.Getter;
</#if>
<#list import as pkg>
import ${pkg};
</#list>

/**
 * <p>
 * ${field.comment!}
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if lombokModel>
@Getter
@AllArgsConstructor
</#if>
<#if (deserializerType??)>
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
    <#if enum.params??>
    ${enum.name}(<#list enum.params as param><#if param.number>${param.value}<#else>"${param.value}"</#if><#if param_has_next>,</#if></#list>)<#if enum_has_next>,<#else>;</#if>
    </#if>
</#list>
<#if !lombokModel>
    ${field.customMap.enumCapitalName}(<#list enumFieldList as param>${param.className} ${param.name}<#if param_has_next>,</#if></#list>){

    }
</#if>

    <#list enumFieldList as param>
    <#if (param.desc??) && param.desc?trim?length gt 0>
    /**
     * ${param.desc}
     */
    </#if>
    private ${param.className} ${param.name};
    </#list>
}