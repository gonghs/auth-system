package com.maple.function.strategy.factory;

import com.maple.common.entity.BaseStrategyFactory;
import com.maple.function.strategy.enums.TestStrategyEnum;
import org.springframework.stereotype.Component;

/**
 * 测试策略工厂
 *
 * @author maple
 * @version 1.0
 * @since 2020-02-26 10:30
 */
@Component
public class TestStrategyFactory extends BaseStrategyFactory<TestStrategyEnum> {
}
