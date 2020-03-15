package com.maple.common.enums;

import com.alibaba.fastjson.annotation.JSONType;
import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * 基础枚举类
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-15 16:26
 */
@JSONType(deserializer = EnumDeserializer.class)
public interface BaseEnum extends IEnum<Integer> {
    /**
     * 返回文字描述
     *
     * @return 描述
     */
    String getDesc();
}
