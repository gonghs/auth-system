package com.maple.common.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 删除缓存注解
 *
 * @author gonghs
 * @version 1.0
 * @since 2019-07-22 10:07
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheEvict {
    /**
     * 存储的key，支持SpEL表达式,允许传入参数为String,String[]或List<String>类型
     */
    String key();
}
