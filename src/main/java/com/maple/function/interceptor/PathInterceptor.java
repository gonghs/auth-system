package com.maple.function.interceptor;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * FIXME 有没有更好的方式实现
 * 路径拦截器 解析控制器RequestMapping值
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-22 17:15
 */
public class PathInterceptor implements HandlerInterceptor {

    private static final String CONTROLLER_URL = "controller-url";


    /**
     * 在视图渲染之前将控制器的url放入 在base-js中取出放入baseUrl中
     *
     * @param request 请求对象
     * @param response 响应对象
     * @param handler 拦截对象
     * @param modelAndView 视图
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (handler instanceof HandlerMethod) {
            Class<?> controller = ((HandlerMethod) handler).getMethod().getDeclaringClass();
            // 使用工具类可以使spring注解AliasFor生效的值也取出来
            RequestMapping requestMapping = AnnotatedElementUtils.getMergedAnnotation(controller,
                    RequestMapping.class);
            String requestUri = request.getRequestURI().replaceAll("/", "");
            String[] strings =
                    Optional.ofNullable(requestMapping).map(RequestMapping::value).orElse(ArrayUtils.EMPTY_STRING_ARRAY);
            for (String mappingUrl : strings) {
                if (StringUtils.contains(requestUri, mappingUrl.replaceAll("/", ""))) {
                    request.setAttribute(CONTROLLER_URL, mappingUrl);
                    return;
                }
            }
        }
        request.setAttribute(CONTROLLER_URL, "");
    }
}
