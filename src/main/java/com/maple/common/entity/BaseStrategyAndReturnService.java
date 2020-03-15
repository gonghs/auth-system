package com.maple.common.entity;

import com.maple.common.enums.BaseStrategyEnum;

/**
 * 策略基础服务类 此处的泛型1只用于为service归类 泛型2用于指定返回类型
 *
 * @author gonghs
 * @version 1.0
 * @since 2020-01-17 14:36
 */
public interface BaseStrategyAndReturnService<E extends BaseStrategyEnum,R>{
    /**
     * 执行并返回值
     *
     * @return 指定的返回类型值
     */
    R exec();
}
