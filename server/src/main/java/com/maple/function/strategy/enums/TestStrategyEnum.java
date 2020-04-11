package com.maple.function.strategy.enums;

import com.maple.common.entity.BaseStrategyService;
import com.maple.common.enums.BaseStrategyEnum;
import com.maple.function.strategy.service.TestService1Impl;
import com.maple.function.strategy.service.TestService2Impl;
import com.maple.utils.SpringUtils;

/**
 * 测试策略枚举
 *
 * @author maple
 * @version 1.0
 * @since 2020-02-26 10:32
 */
public enum TestStrategyEnum implements BaseStrategyEnum {
    /**
     * 枚举信息
     */
    SERVICE1(TestService1Impl.class, "测试策略1"), SERVICE2(TestService2Impl.class, "测试策略2");

    TestStrategyEnum(Class<? extends BaseStrategyService> clazz, String strategyDesc) {
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
