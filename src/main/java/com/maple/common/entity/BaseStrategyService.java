package com.maple.common.entity;

import com.maple.common.enums.BaseStrategyEnum;

/**
 * 策略接触服务类 此处的泛型只用于为service归类
 *
 * @author gonghs
 * @version 1.0
 * @since 2020-01-17 14:36
 */
public interface BaseStrategyService<E extends BaseStrategyEnum> extends BaseStrategyAndReturnService<E, Void>{
    /**
     * 执行策略方法
     *
     * @return 返回空类型
     */
    @Override
    Void exec();
}
