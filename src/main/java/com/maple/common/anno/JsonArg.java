package com.maple.common.anno;

import java.lang.annotation.*;

/**
 * 此注解用于解决 @RequestBody不支持多对象的问题(本质上还是使用mybatis-plus分页时不希望DTO对象强制继承page)
 *
 * @author gonghs
 * @version 1.0
 * @since 2019-07-16 15:25
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonArg {
}
