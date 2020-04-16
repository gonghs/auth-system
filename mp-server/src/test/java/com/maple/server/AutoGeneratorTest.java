package com.maple.server;

import com.maple.starter.generator.CodeGeneratorUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 代码生成器
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-09 00:16
 */
public class AutoGeneratorTest extends BaseTest {
    @Autowired
    private CodeGeneratorUtils codeGeneratorUtils;

    @Test
    public void generator() {
        codeGeneratorUtils.generator();
    }
}
