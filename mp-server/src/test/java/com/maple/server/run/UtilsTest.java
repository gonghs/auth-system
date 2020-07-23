package com.maple.server.run;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.maple.server.dto.base.BaseDTO;
import com.maple.server.function.auth.MyRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

/**
 * huTools/apacheUtils 测试
 *
 * @author maple
 * @version 1.0
 * @since 2020-02-27 11:52
 */
@Slf4j
public class UtilsTest {
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

    @Test
    public void testBeanUtil() {
        MyRealm bean = new MyRealm();
        bean.setCachingEnabled(false);
        BeanUtil.setProperty(bean, "cachingEnabled", true);
        BeanUtil.setProperty(bean, "caching-enabled", true);
        log.info(bean.toString());
    }

    @Test
    public void testSub() {
        String string = "aaa_111_111";
        log.info("切割前: {}",string);
        log.info("切割后: {}", StrUtil.subBefore(string, "_", true));
    }

    @Test
    public void testEqualAny() {
        String[] arr = {"pdf", "doc", "docx"};
        log.info("{}",StringUtils.equalsAny("docx", arr));
    }

    @Test
    public void testBeanPath() {
        BaseDTO baseDTO = new BaseDTO();
        baseDTO.setDesc("nihao");
        List<BaseDTO> baseDTOS = Arrays.asList(baseDTO);
        BeanUtil.getProperty(baseDTOS, "me[0].desc");
        log.info("end");
    }
}
