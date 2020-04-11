package com.maple.function.strategy;

import com.maple.BaseTest;
import com.maple.function.strategy.enums.TestStrategy2Enum;
import com.maple.function.strategy.enums.TestStrategyEnum;
import com.maple.function.strategy.factory.TestStrategy2Factory;
import com.maple.function.strategy.factory.TestStrategyFactory;
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
        testStrategyFactory.getByType(TestStrategyEnum.SERVICE1).exec();
        testStrategyFactory.getByType(TestStrategyEnum.SERVICE2).exec();
        testStrategyFactory.getByType(TestStrategy2Enum.SERVICE1).exec();
        testStrategy2Factory.getByType(TestStrategy2Enum.SERVICE1).exec();
        testStrategy2Factory.getByType(TestStrategy2Enum.SERVICE2).exec();
    }
}
