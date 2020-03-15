package com.maple.common.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

import java.io.Serializable;

/**
 * 基础策略枚举
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-15 16:26
 */
public interface BaseStrategyEnum extends IEnum<String>, Serializable {
    /**
     * 获取策略描述
     *
     * @return 策略描述
     */
    default String getStrategyDesc() {
        return "";
    }
}
