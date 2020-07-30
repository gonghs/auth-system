package com.maple.starter.generator.ext;

import com.maple.starter.generator.constant.TemplateType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 自定义生成模板
 *
 * @author maple
 * @version 1.0
 * @since 2020-07-30 00:00
 */
@Setter
@Getter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class CustomTemplate {
    /**
     * 选择效仿的模板类型
     */
    private TemplateType templateType = TemplateType.ENTITY;
    /**
     * 文件和类名
     */
    private String name = "%sReqDTO";
}
