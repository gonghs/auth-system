package com.maple.server.function.strategy.service;

import com.maple.server.common.entity.BaseStrategyService;
import com.maple.server.function.strategy.enums.TestStrategyEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 测试策略1
 *
 * @author maple
 * @version 1.0
 * @since 2020-02-26 10:39
 */
@Service
@Slf4j
public class TestService2Impl implements BaseStrategyService<TestStrategyEnum> {
    @Override
    public Void exec() {
        log.info("service2");
        return null;
    }
}
