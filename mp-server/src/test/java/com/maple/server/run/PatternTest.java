package com.maple.server.run;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则测试
 *
 * @author maple
 * @version 1.0
 * @since 2020-06-18 15:37
 */
public class PatternTest {
    Pattern pattern = Pattern.compile("([0-9]:[^,]*(?:,))*?([0-9]:[^,]*?)(?=;)");
    Pattern patternNum = Pattern.compile("([0-9]*?)*");
    Pattern pattern1 = Pattern.compile("([0-9]:[^,]*?,)");


    private final static Pattern DICT_PATTERN = Pattern.compile("[@]Dict[{]([a-zA-Z0-9._]+)[}]");
    public final static Pattern REG_EX_VAL = Pattern.compile("#(.*?\\{(.*?)?\\})");
    public final static Pattern REG_EX_KEY = Pattern.compile("([A-Za-z1-9_-]+):(.*?)?;");

    @Test
    public void testDict() {
        Matcher m = DICT_PATTERN.matcher("@Dict{a}");
        if (m.find()) {
            System.out.println("Found value: " + m.group(0));
            System.out.println("Found value: " + m.group(1));
            System.out.println("Found value: " + m.group(2));
            System.out.println("Found value: " + m.group(3));
        } else {
            System.out.println("NO MATCH");
        }
    }

    @Test
    public void testKeyAndVal() {
        Matcher m = REG_EX_VAL.matcher("#Test{1:你好,nihao}#Test{2:你不好,nibuhao}");
        while (m.find()) {
            System.out.println("Found value: " + m.group(0));
            System.out.println("Found value: " + m.group(1));
            System.out.println("Found value: " + m.group(2));
            String val = m.group(2);
            if (!val.endsWith(";")) {
                val += ";";
            }
            Matcher matcher = REG_EX_KEY.matcher(val);
            while (matcher.find()) {
                System.out.println("REG_EX_KEY Found value: " + matcher.group(0));
                System.out.println("REG_EX_KEY Found value: " + matcher.group(1));
                System.out.println("REG_EX_KEY Found value: " + matcher.group(2));
            }
        }
    }


    @Test
    public void testPattern() {
        System.out.println("数据状态 1:正常,2:正常,3:删除;".matches("([0-9]:[\\s\\S]*?,)*?[0-9]:[\\s\\S]*?;"));
        System.out.println(Pattern.matches("[\\s\\S]*?([0-9]:[\\s\\S]*?,)*?[0-9]:[\\s\\S]*?;[\\s\\S]*?", "数据状态 1:正常," +
                "2:删除;"));
        System.out.println(Pattern.matches("[\\s\\S]*?([0-9]:[\\s\\S]*?,)*?([0-9]:[\\s\\S]*?);[\\s\\S]*?", "数据状态 " +
                "1:正常;" +
                " 数据"));
        Matcher m = pattern.matcher("数据状态 1:正常,2:正常,3:删除;");
        if (m.find()) {
            System.out.println("Found value: " + m.group(0));
            System.out.println("Found value: " + m.group(1));
            System.out.println("Found value: " + m.group(2));
            System.out.println("Found value: " + m.group(3));
        } else {
            System.out.println("NO MATCH");
        }
    }

}
