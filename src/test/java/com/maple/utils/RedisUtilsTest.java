package com.maple.utils;

import com.maple.BaseTest;
import com.maple.dto.base.BaseDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * redis工具测试
 *
 * @author gonghs
 * @version 1.0
 * @since 2020-04-04 14:12
 */
public class RedisUtilsTest extends BaseTest {
    @Autowired
    private RedisUtils redisUtils;
    private List<BaseDTO> testObj;
    private final String testKey = this.getClass().getName();
    @Before
    public void before() {
        BaseDTO baseDTO = new BaseDTO();
        baseDTO.setId(1L);
        testObj = Collections.singletonList(baseDTO);
    }

    @Test
    public void setAndGet() throws InterruptedException {
        redisUtils.set(testKey, testObj);
        redisUtils.set(testKey + '1', testObj, 1, TimeUnit.MILLISECONDS);
        Thread.sleep(1);
        List<BaseDTO> resultDTO = redisUtils.get(testKey);
        Assert.assertNotNull(resultDTO);
        Assert.assertNotNull(resultDTO.get(0));
        Assert.assertNotNull(resultDTO.get(0).getId());
        Object resultDTO1 = redisUtils.get(testKey + '1');
        Assert.assertNull(resultDTO1);
    }

    @Test
    public void del() {
        redisUtils.del(testKey);
        Assert.assertNull(redisUtils.get(testKey));
    }
}