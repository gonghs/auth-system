package com.maple.server.common.entity;

/**
 * 一个无输入参数,无输出参数的策略服务接口
 *
 * @author gonghs
 * @version 1.0
 * @since 2020-01-17 14:36
 */
public interface StrategyService {
    /**
     * 执行策略方法
     */
    default void exec() {
    }
}
