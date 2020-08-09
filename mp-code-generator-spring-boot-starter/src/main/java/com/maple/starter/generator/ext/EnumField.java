package com.maple.starter.generator.ext;

import cn.hutool.core.util.StrUtil;
import com.maple.common.constant.SymbolConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 枚举字段配置
 *
 * @author maple
 * @version 1.0
 * @since 2020-07-29 23:04
 */
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class EnumField implements Serializable {
    /**
     * 字段类型 至少需要有一条数据指定类型为code
     */
    private EnumFieldTypeEnum type;
    /**
     * 字段类名
     */
    private String clazz = "java.lang.String";
    /**
     * 字段名
     */
    private String name;
    /**
     * 如果需要默认值(即不从注释读取值)才定义 若定义则不会再动态赋值
     */
    private String value;
    /**
     * 字段描述
     */
    private String desc;

    public boolean isNumber() {
        try {
            return Number.class.isAssignableFrom(Class.forName(clazz));
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public String getClassName() {
        return StrUtil.subAfter(clazz, SymbolConst.POINT, true);
    }

    public static EnumField code(String className, String name, String desc) {
        return new EnumField(EnumFieldTypeEnum.CODE, className, name, null, desc);
    }

    public static EnumField other(String className, String name, String desc) {
        return new EnumField(EnumFieldTypeEnum.OTHER, className, name, null, desc);
    }

    public enum EnumFieldTypeEnum {
        /**
         * 枚举字段类型 code 描述 其他 用于辅助排序 code将排在第一个对应最左测数字 desc次之
         * 除此之外 code类型字段的类可用于接口泛型参数 例如code类型字段clazz为java.lang.Integer 接口为IEnum<%s> 生成后将被替换为IEnum<Integer>
         * 如有 1:有效,2:无效; 则生成枚举参数为 (1,有效)(2,无效)
         */
        CODE, DESC, OTHER
    }
}
