package com.maple.function.interceptor;

import com.maple.common.context.RequestContext;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 日志拦截器
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-02 17:38
 */
public class LogInterceptor implements HandlerInterceptor {
    /**
     * request id 在日志全局变量中的key
     */
    private static final String REQUEST_ID_KEY = "requestId";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放入请求id
        String requestId = UUID.randomUUID().toString();
        MDC.put(REQUEST_ID_KEY, requestId);
        RequestContext.setRequestId(requestId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {
        MDC.remove(REQUEST_ID_KEY);
        // 清理本地线程对象
        RequestContext.remove();
    }
}
