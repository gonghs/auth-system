package com.maple.server.function.strategy.enums;

import com.maple.server.common.enums.BaseStrategyEnum;
import com.maple.server.function.strategy.service.TestService1Impl;
import com.maple.server.function.strategy.service.TestService2Impl;
import com.maple.server.utils.SpringUtils;

/**
 * 测试策略枚举
 *
 * @author maple
 * @version 1.0
 * @since 2020-02-26 10:32
 */
public enum TestStrategy2Enum implements BaseStrategyEnum {
    /**
     * 枚举信息
     */
    SERVICE1(TestService1Impl.class, "测试策略1"), SERVICE2(TestService2Impl.class, "测试策略2");

    TestStrategy2Enum(Class<?> clazz, String strategyDesc) {
        this.value = SpringUtils.getBeanNameByType(clazz);
        this.strategyDesc = strategyDesc;
    }

    /**
     * 策略值
     */
    private String value;
    /**
     * 策略描述
     */
    private String strategyDesc;

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getStrategyDesc() {
        return strategyDesc;
    }
}
