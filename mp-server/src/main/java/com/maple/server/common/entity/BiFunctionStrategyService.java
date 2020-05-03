package com.maple.server.common.entity;

/**
 * 一个单输入参数,双输出参数的策略服务接口
 *
 * @author maple
 * @version 1.0
 * @since 2020-01-17 14:36
 */
public interface BiFunctionStrategyService<T, U, R> {
    /**
     * 执行策略方法
     *
     * @param t 输入参数
     * @param u 输入参数2
     * @return r 输出参数
     */
    R exec(T t, U u);
}
