package com.maple.server.run;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.maple.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.function.Function;

/**
 * 函数式接口测试
 *
 * @author maple
 * @version 1.0
 * @since 2020-04-22 15:19
 */
@Slf4j
public class FunctionalTest {

    @Test
    public void test() {
        fun(str -> {
            String a = null;
            a.toString();
            throw new ServiceException("出错了");
        });
    }

    private void fun(Function<String, String> function) {
        try {
            function.apply("你好");
        } catch (Exception e) {
            ExceptionUtil.getMessage(e);
            log.error("捕获到异常", e);
        }
    }

}
