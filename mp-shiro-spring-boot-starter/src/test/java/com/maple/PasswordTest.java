package com.maple;

import com.maple.starter.shiro.utils.ShiroUtils;
import org.junit.Test;

/**
 * 密码测试类
 *
 * @author maple
 * @version 1.0
 * @since 2020-07-01 10:42
 */
public class PasswordTest {
    @Test
    public void gen() {
        System.out.println(ShiroUtils.genMd5Password("1", "maple",2));
    }
}
