package com.maple.function.strategy.enums;

import com.maple.common.entity.BaseStrategyService;
import com.maple.common.enums.BaseStrategyEnum;
import com.maple.function.strategy.service.TestService3Impl;
import com.maple.function.strategy.service.TestService4Impl;
import com.maple.utils.SpringUtils;

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
    SERVICE1(TestService3Impl.class, "测试策略3"), SERVICE2(TestService4Impl.class, "测试策略4");

    TestStrategy2Enum(Class<? extends BaseStrategyService> clazz, String strategyDesc) {
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
