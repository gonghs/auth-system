package com.maple.server;

import com.maple.starter.generator.CodeGeneratorAutoConfiguration;
import com.maple.starter.generator.CodeGeneratorUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 代码生成器
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-09 00:16
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodeGeneratorAutoConfiguration.class)
public class AutoGeneratorTest {
    @Autowired
    private CodeGeneratorUtils codeGeneratorUtils;

    @Test
    public void generator() {
        codeGeneratorUtils.generator();
    }
}
