package com.maple.server.common.anno;

import java.lang.annotation.*;

/**
 * 用户上下文注入标识
 *
 * @author maple
 * @version 1.0
 * @since 2019-07-16 15:25
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {
}
