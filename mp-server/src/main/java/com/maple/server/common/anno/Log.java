package com.maple.server.common.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * log注解
 *
 * @author maple
 * @version 1.0
 * @since 2019-07-22 10:07
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    /**
     * 是否开启入参打印
     */
    boolean enableInputParams() default true;

    /**
     * 是否开启出参打印
     */
    boolean enableOutputParams() default true;

    /**
     * 是否开启执行时间打印
     */
    boolean enableExecuteTime() default true;
}
