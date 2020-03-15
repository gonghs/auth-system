package com.maple.config;

import com.google.common.collect.Lists;
import com.maple.common.anno.JsonArg;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

import static com.google.common.collect.Lists.newArrayList;

/**
 * swagger插件  解决swagger生成文档时参数传输问题(不修正则使用表单提交 应为json提交)
 * 参考源码 spring fox-spring-web OperationParameterReader
 *
 * @author gonghs
 * @version 1.0
 * @since 2019-09-23 09:56
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class JsonArgOperationBuilderPlugin implements OperationBuilderPlugin {
    private final DocumentationPluginsManager pluginsManager;

    public JsonArgOperationBuilderPlugin(DocumentationPluginsManager pluginsManager) {
        this.pluginsManager = pluginsManager;
    }

    @Override
    public void apply(OperationContext context) {
        List<ResolvedMethodParameter> parameters = context.getParameters();
        if (parameters.stream().noneMatch(item -> item.hasParameterAnnotation(JsonArg.class))) {
            return;
        }
        // 使用反射清空原本生成的字段(目前没有更好的方式)
        Field parametersField = ReflectionUtils.findField(context.operationBuilder().getClass(),
                field -> Objects.equals("parameters", field.getName()));
        Objects.requireNonNull(parametersField, "反射获取字段失败");
        ReflectionUtils.setField(parametersField, context.operationBuilder(), Lists.newArrayList());
        context.operationBuilder().parameters(readParameters(context));
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    private List<Parameter> readParameters(final OperationContext context) {
        List<ResolvedMethodParameter> methodParameters = context.getParameters();
        List<Parameter> parameters = newArrayList();
        for (ResolvedMethodParameter methodParameter : methodParameters) {
            if (!methodParameter.hasParameterAnnotation(JsonArg.class)) {
                continue;
            }
            ParameterContext parameterContext = new ParameterContext(methodParameter,
                    new ParameterBuilder(),
                    context.getDocumentationContext(),
                    context.getGenericsNamingStrategy(),
                    context);
            pluginsManager.parameter(parameterContext);
            parameters.add(parameterContext.parameterBuilder().build());
        }
        return parameters;
    }
}
