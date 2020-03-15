package com.maple.function.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 日志切面
 *
 * @author gonghs
 * @version 1.0
 * @since 2020-02-24 17:00
 */
@Aspect
@Component
public class LogAspect {
    @Around("execution(* com.maple*..service*..*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        return pjp.proceed();
    }
}
