package com.maple.server.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * 请求工具类
 *
 * @author maple
 * @version 1.0
 * @since 2019-10-04 14:33
 */
@UtilityClass
public class RequestUtils {
    /**
     * 功能描述: 判断请求是否是ajax
     *
     * @param request 请求信息
     * @return 是否ajax请求
     */
    public boolean isAjax(ServletRequest request) {
        final String ajaxHeader = "XMLHttpRequest";
        String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
        return StringUtils.equalsIgnoreCase(ajaxHeader, header);
    }
}
