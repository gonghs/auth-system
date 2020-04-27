package com.maple.server.function.strategy.factory;

import com.maple.server.common.entity.BaseStrategyFactory;
import com.maple.server.common.entity.FunctionStrategyService;
import com.maple.server.function.strategy.enums.TestStrategyEnum;
import org.springframework.stereotype.Component;

/**
 * 测试策略工厂
 *
 * @author maple
 * @version 1.0
 * @since 2020-02-26 10:30
 */
@Component
public class TestStrategyFactory extends BaseStrategyFactory<TestStrategyEnum, FunctionStrategyService<Integer, Integer>> {
}
