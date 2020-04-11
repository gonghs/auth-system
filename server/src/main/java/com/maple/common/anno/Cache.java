package com.maple.common.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存注解
 *
 * @author maple
 * @version 1.0
 * @since 2019-07-22 10:07
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {
    /**
     * 存储的key，支持SpEL表达式。
     */
    String key();

    /**
     * 过期时间
     */
    int ttlMinutes();
}
