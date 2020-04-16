package com.maple.server.common.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解作用与方法用于给方法入参增加前缀
 *
 * @author maple
 * @version 1.0
 * @since 2019-07-22 10:07
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Prefix {
    /**
     * 需要加前缀的参数名
     */
    String value();

    /**
     * 前缀key值 会从配置去取
     */
    String prefix() default "";

    /**
     * 是否给空字串加前缀
     */
    boolean ignoreBlank() default true;
}
