package com.maple.function.strategy.service;

import com.maple.common.entity.BaseStrategyService;
import com.maple.function.strategy.enums.TestStrategy2Enum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 测试策略1
 *
 * @author gonghs
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
