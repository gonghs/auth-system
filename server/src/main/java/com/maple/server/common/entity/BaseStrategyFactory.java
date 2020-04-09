package com.maple.server.common.entity;

import com.maple.server.common.enums.BaseStrategyEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 基础策略类
 *
 * @author maple
 * @version 1.0
 * @since 2020-01-17 14:29
 */
@Slf4j
public class BaseStrategyFactory<E extends BaseStrategyEnum> {
    @Autowired(required = false)
    private Map<String, BaseStrategyAndReturnService<E, ?>> strategyMap;

    /**
     * 根据类型获取策略服务
     *
     * @param type 类型
     * @return 策略服务类
     */
    public BaseStrategyAndReturnService getByType(BaseStrategyEnum type) {
        return strategyMap.getOrDefault(type.getValue(), () -> {
            log.info("该策略未找到实现类");
            return null;
        });
    }
}
