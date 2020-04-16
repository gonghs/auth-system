package com.maple.common.context;



/**
 * request相关上下文
 *
 * @author maple
 * @version 1.0
 * @since 2019-07-31 17:20
 */
public class RequestContext {
    /**
     * 请求id 本地线程对象
     */
    private static ThreadLocal<String> requestIdThreadLocal = new ThreadLocal<>();

    public static void setRequestId(String requestId) {
        requestIdThreadLocal.set(requestId);
    }

    public static String getRequestId() {
        return requestIdThreadLocal.get();
    }

    public static void remove() {
        requestIdThreadLocal.remove();
    }
}
