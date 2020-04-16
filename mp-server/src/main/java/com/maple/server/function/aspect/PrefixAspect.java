package com.maple.server.function.aspect;

import com.maple.common.constant.SymbolConst;
import com.maple.server.common.anno.Prefix;
import com.maple.server.utils.SpringElUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 前缀切面
 *
 * @author maple
 * @version 1.0
 * @since 2020-04-04 15:58
 */
@Slf4j
@Aspect
@Component
public class PrefixAspect {
    @Autowired
    private Environment environment;

    @Around("@annotation(prefix)")
    public Object cache(ProceedingJoinPoint joinPoint, Prefix prefix) throws Throwable {
        if (StringUtils.isBlank(prefix.prefix())) {
            return joinPoint.proceed();
        }
        String prefixStr = environment.getProperty(prefix.prefix());
        if (StringUtils.isBlank(prefixStr)) {
            return joinPoint.proceed();
        }
        Object key = SpringElUtils.parseKey(SymbolConst.POUND + prefix.value(),
                ((MethodSignature) joinPoint.getSignature()).getMethod(), joinPoint.getArgs());
        if (!(key instanceof Collection) && !(key instanceof String)) {
            return joinPoint.proceed();
        }
        Object o = proceedByType(prefixStr, key, prefix);
        if (Objects.isNull(o)) {
            return joinPoint.proceed();
        }
        // 替换参数
        Object[] resultArgs = SpringElUtils.replaceArg(prefix.value(),
                ((MethodSignature) joinPoint.getSignature()).getMethod(),
                joinPoint.getArgs(), o);
        return joinPoint.proceed(resultArgs);
    }

    private Object proceedByType(String prefixStr, Object key, Prefix prefix) {
        if (key instanceof Collection) {
            try {
                @SuppressWarnings("unchecked cast")
                Collection<String> keyColl = (Collection<String>) key;
                return keyColl.stream().map(item -> proceedByType(prefixStr, item, prefix)).collect(Collectors.toList());
            } catch (Exception e) {
                return null;
            }
        }
        if (key instanceof String) {
            String keyStr = (String) key;
            return prefix.ignoreBlank() && StringUtils.isBlank(keyStr) ? null : prefixStr + keyStr;
        }
        return null;
    }
}
