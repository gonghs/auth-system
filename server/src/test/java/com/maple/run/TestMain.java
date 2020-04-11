package com.maple.run;

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
public class TestMain {
    @Test
    public void testClassUtils() {
        System.out.println(ClassUtils.isAssignable(new Class[]{ModelAndView.class, String.class}, ModelAndView.class,String.class));
        System.out.println(ClassUtils.isAssignable(new Class[]{ModelAndView.class, String.class}, ModelAndView.class));
        System.out.println(ClassUtils.isAssignable(ModelAndView.class, ModelAndView.class));
        System.out.println(ClassUtils.isAssignable(String.class, ModelAndView.class));
    }
}
