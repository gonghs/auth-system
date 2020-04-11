package com.maple.server;

import com.maple.starter.generator.CodeGeneratorUtils;
import com.maple.starter.generator.properties.CodeGeneratorProperties;
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
    private CodeGeneratorProperties codeGeneratorProperties;
    @Autowired
    private CodeGeneratorUtils codeGeneratorUtils;

    @Test
    public void generator() {
        codeGeneratorProperties.setGenTables(new String[]{"test"});
        codeGeneratorProperties.setModelName("test");
        codeGeneratorUtils.generator();
    }
}
