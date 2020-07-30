package com.maple.starter.generator.constant;

import com.baomidou.mybatisplus.generator.config.ConstVal;
import lombok.Getter;

/**
 * 模板类型
 *
 * @author maple
 * @version 1.0
 * @since 2020-07-29 23:54
 */
@Getter
public enum TemplateType {
    /**
     *
     */
    ENTITY(ConstVal.ENTITY), MAPPER(ConstVal.MAPPER), SERVICE(ConstVal.SERVICE), SERVICE_IMPL(ConstVal.SERVICE_IMPL), CONTROLLER(ConstVal.CONTROLLER);

    TemplateType(String templateKey) {
        this.templateKey = templateKey;
    }

    private final String templateKey;
}
