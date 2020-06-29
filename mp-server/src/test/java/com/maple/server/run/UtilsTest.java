package com.maple.server.run;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.maple.server.common.entity.MyRealm;
import com.maple.starter.generator.properties.CodeGeneratorProperties;
import com.maple.starter.generator.utils.YmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * huTools/apacheUtils 测试
 *
 * @author maple
 * @version 1.0
 * @since 2020-02-27 11:52
 */
@Slf4j
public class UtilsTest {
    Pattern pattern = Pattern.compile("([0-9]:[^,]*(?:,))*?([0-9]:[^,]*?)(?=;)");
    Pattern patternNum = Pattern.compile("([0-9]*?)*");
    Pattern pattern1 = Pattern.compile("([0-9]:[^,]*?,)");
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
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("id+", "id");
        tempMap.put("flag", 1);
        System.out.println(BeanUtil.getProperty(tempMap, "#id + '1'"));
    }

    @Test
    public void testYmlUtils() {
        Map map = YmlUtils.loadClass(Map.class);
        Object property = BeanUtil.getProperty(map, "mp.tool.generator");
        CodeGeneratorProperties convert = JSONUtil.toBean(JSONUtil.parse(property), CodeGeneratorProperties.class,false);
    }
}
