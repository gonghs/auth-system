package com.maple.server.run;

import cn.hutool.core.util.URLUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

/**
 * 直接运行的测试类
 *
 * @author maple
 * @version 1.0
 * @since 2020-02-27 11:52
 */
@Slf4j
public class TestMain {
    @Test
    public void testClassUtils() {
        System.out.println(ClassUtils.isAssignable(new Class[]{ModelAndView.class, String.class}, ModelAndView.class,String.class));
        System.out.println(ClassUtils.isAssignable(new Class[]{ModelAndView.class, String.class}, ModelAndView.class));
        System.out.println(ClassUtils.isAssignable(ModelAndView.class, ModelAndView.class));
        System.out.println(ClassUtils.isAssignable(String.class, ModelAndView.class));
    }

    @Test
    public void testUrlUtil() {
        log.info(URLUtil.getURL("/src/main/java").toString());
    }
}
