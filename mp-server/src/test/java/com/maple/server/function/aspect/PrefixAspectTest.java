package com.maple.server.function.aspect;

import com.maple.server.BaseTest;
import com.maple.server.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 前缀切面测试
 *
 * @author maple
 * @version 1.0
 * @since 2020-04-06 13:51
 */
@Slf4j
@Configuration
public class PrefixAspectTest extends BaseTest {
    private final String testKey = this.getClass().getName();

    @Autowired
    private Environment environment;
    @Autowired
    private RedisUtils redisUtils;

    @Test
    public void env() {
        String prefix = environment.getProperty("cache.prefix");
        log.info("prefix: {}", prefix);
    }

    @Test
    public void prefix() {
        redisUtils.set(testKey, "测试prefix");
        redisUtils.get(testKey);
        redisUtils.del(testKey);
        redisUtils.set("", "测试prefix1");
    }
}