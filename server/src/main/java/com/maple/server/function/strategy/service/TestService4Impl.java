package com.maple.server.function.strategy.service;

import com.maple.server.common.entity.BaseStrategyService;
import com.maple.server.function.strategy.enums.TestStrategy2Enum;
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
public class TestService4Impl implements BaseStrategyService<TestStrategy2Enum> {
    @Override
    public Void exec() {
        log.info("service4");
        return null;
    }
}
