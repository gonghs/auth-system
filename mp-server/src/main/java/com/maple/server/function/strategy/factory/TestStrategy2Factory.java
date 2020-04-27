package com.maple.server.function.strategy.factory;

import com.maple.server.common.entity.BaseStrategyFactory;
import com.maple.server.common.entity.StrategyService;
import com.maple.server.function.strategy.enums.TestStrategy2Enum;
import org.springframework.stereotype.Component;

/**
 * 测试策略工厂
 *
 * @author maple
 * @version 1.0
 * @since 2020-02-26 10:30
 */
@Component
public class TestStrategy2Factory extends BaseStrategyFactory<TestStrategy2Enum, StrategyService> {
}
