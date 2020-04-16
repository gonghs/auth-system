package com.maple.server.config;

import com.maple.server.common.anno.JsonArg;
import org.springframework.stereotype.Component;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

import static springfox.documentation.swagger.common.SwaggerPluginSupport.pluginDoesApply;

/**
 * swagger插件  解决swagger生成文档时参数传输问题(不修正则使用表单提交 应为json提交)
 * 参考源码 spring fox-spring-web ParameterTypeReader
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-23 09:56
 */
@Component
public class JsonArgParameterBuilderPlugin implements ParameterBuilderPlugin {

    @Override
    public void apply(ParameterContext parameterContext) {
        ResolvedMethodParameter resolvedMethodParameter = parameterContext.resolvedMethodParameter();
        if (!resolvedMethodParameter.hasParameterAnnotation(JsonArg.class)) {
            return;
        }
        parameterContext.parameterBuilder().parameterType("body");
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return pluginDoesApply(delimiter);
    }
}
