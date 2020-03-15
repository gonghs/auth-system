package com.maple.function.strategy.service;

import com.maple.common.entity.BaseStrategyService;
import com.maple.function.strategy.enums.TestStrategyEnum;
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
public class TestService1Impl implements BaseStrategyService<TestStrategyEnum> {
    @Override
    public Void exec() {
        log.info("service1");
        return null;
    }
}
