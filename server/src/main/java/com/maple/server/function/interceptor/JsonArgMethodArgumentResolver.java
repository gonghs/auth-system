package com.maple.server.function.interceptor;

import com.alibaba.fastjson.JSON;
import com.maple.server.common.anno.JsonArg;
import com.maple.server.common.constant.GlobalConst;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;


/**
 * 此方法解析器用于解决 @RequestBody不支持多对象的问题(本质上还是使用mybatis-plus分页时不希望DTO对象强制继承page)
 *
 * @author maple
 * @version 1.0
 * @since 2019-07-16 15:20
 */
public class JsonArgMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String JSON_BODY_ATTRIBUTE = "JSON_REQUEST_BODY";

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(JsonArg.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        String bodyJson = getRequestBody(nativeWebRequest);
        Object o = JSON.parseObject(bodyJson, methodParameter.getParameterType());
        // 防止空指针
        return Optional.ofNullable(o).orElse(methodParameter.getParameterType().newInstance());
    }

    private String getRequestBody(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Optional.ofNullable(servletRequest).orElseThrow(() -> new RuntimeException("获取请求对象失败 !!!"));
        String jsonBody = (String) webRequest.getAttribute(JSON_BODY_ATTRIBUTE, NativeWebRequest.SCOPE_REQUEST);
        if (Objects.isNull(jsonBody)) {
            try {
                // 此值只能读取一次 因此读取完毕 放回去第二次从该处读取
                jsonBody = IOUtils.toString(servletRequest.getInputStream(), GlobalConst.DEFAULT_CHARSET);
                webRequest.setAttribute(JSON_BODY_ATTRIBUTE, jsonBody, NativeWebRequest.SCOPE_REQUEST);
                return jsonBody;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return jsonBody;

    }
}
