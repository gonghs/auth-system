package com.maple.config;

import com.fasterxml.classmate.ResolvedType;
import com.maple.common.anno.JsonArg;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.OperationModelsProviderPlugin;
import springfox.documentation.spi.service.contexts.RequestMappingContext;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * swagger出入参插件  解决swagger生成文档时参数传输问题(不修正则使用表单提交 应为json提交)
 * 参考源码 spring fox-spring-web OperationModelsProvider
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-23 09:56
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class JsonArgModelsPlugin implements OperationModelsProviderPlugin {

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked cast")
    public void apply(RequestMappingContext context) {
        // 合并参数
        ModelContext modelContext = mergeParam(context);
        if (Objects.isNull(modelContext)) {
            return;
        }
        // 使用反射写入参数
        Set<ModelContext> o = (Set<ModelContext>) FieldUtils.readDeclaredField(context.operationModelsBuilder(),
                "contexts", true);
        o.add(modelContext);
        FieldUtils.writeDeclaredField(context.operationModelsBuilder(), "contexts", o, true);
    }

    /**
     * 合并参数为ModelContext对象
     */
    @SneakyThrows
    public ModelContext mergeParam(RequestMappingContext context) {
        ModelContext modelContext = null;
        List<ResolvedMethodParameter> mergeList =
                context.getParameters().stream().filter(item -> item.hasParameterAnnotation(JsonArg.class)).collect(Collectors.toList());
        // 将所有带有JsonArg注解的参数收集为一个modelContext
        for (ResolvedMethodParameter item : mergeList) {
            ResolvedType modelType = context.alternateFor(item.getParameterType());
            ModelContext inputParam = ModelContext.inputParam(context.getGroupName(), modelType,
                    context.getDocumentationContext().getDocumentationType(),
                    context.getDocumentationContext().getAlternateTypeProvider(), context.getGenericsNamingStrategy(),
                    context.getIgnorableParameterTypes());
            modelContext = modelContext == null ? inputParam : ModelContext.fromParent(modelContext, modelType);
        }

        return modelContext;
    }
}
