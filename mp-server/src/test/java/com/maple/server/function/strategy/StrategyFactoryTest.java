package com.maple.server.function.strategy;

import com.maple.server.BaseTest;
import com.maple.server.function.strategy.enums.TestStrategy2Enum;
import com.maple.server.function.strategy.enums.TestStrategyEnum;
import com.maple.server.function.strategy.factory.TestStrategy2Factory;
import com.maple.server.function.strategy.factory.TestStrategyFactory;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 策略工厂测试
 *
 * @author maple
 * @version 1.0
 * @since 2020-02-26 10:45
 */
public class StrategyFactoryTest extends BaseTest {
    @Autowired
    private TestStrategyFactory testStrategyFactory;
    @Autowired
    private TestStrategy2Factory testStrategy2Factory;

    @Test
    public void testService() {
        Integer execRs = testStrategyFactory.getByType(TestStrategyEnum.SERVICE1).exec(1);
        Integer execRs2 = testStrategyFactory.getByType(TestStrategyEnum.SERVICE2).exec(2);
        Assert.assertEquals(execRs.intValue(), 1);
        Assert.assertEquals(execRs2.intValue(), 2);
        testStrategy2Factory.getByType(TestStrategy2Enum.SERVICE1).exec();
        testStrategy2Factory.getByType(TestStrategy2Enum.SERVICE2).exec();
    }
}
