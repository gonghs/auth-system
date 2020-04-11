package com.maple.function.aspect;

import com.maple.common.anno.CacheEvict;
import com.maple.utils.SpringElUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 缓存删除切面
 * <p>
 * 由于spring-cache不支持自定义ttl 因此采用自定义方式实现
 * 允许使用SpEl表达式设置key:(支持传入String,String[]或List<String>两种形式的key)
 *
 * @author maple
 * @version 1.0
 * @since 2019-07-22 10:08
 */
@Aspect
@Component
public class CacheEvictAspect {
    private final String prefix;
    private final StringRedisTemplate stringRedisTemplate;

    public CacheEvictAspect(StringRedisTemplate stringRedisTemplate, @Value("${cache.prefix:}") String prefix) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.prefix = prefix;
    }

    @Around("@annotation(cacheEvict)")
    public Object cache(ProceedingJoinPoint joinPoint, CacheEvict cacheEvict) throws Throwable {
        // 校验缓存key是否有效
        checkCacheArgs(cacheEvict);
        Object key = SpringElUtils.parseKey(cacheEvict.key(), ((MethodSignature) joinPoint.getSignature()).getMethod(),
                joinPoint.getArgs());
        if (key instanceof String) {
            if (StringUtils.isBlank((String) key)) {
                throw new RuntimeException("缓存key不能为空");
            }
            stringRedisTemplate.delete(joinKey((String) key));
        } else if (key instanceof List<?>) {
            ((List<?>) key).forEach(item -> {
                if (item instanceof String) {
                    stringRedisTemplate.delete(joinKey((String) item));
                }
            });
        } else if (key instanceof String[]) {
            for (String item : ((String[]) key)) {
                stringRedisTemplate.delete(joinKey(item));
            }
        } else {
            throw new RuntimeException("缓存key值不合法,须为String,String[]或List<String>类型");
        }
        return joinPoint.proceed();
    }

    private String joinKey(String key) {
        return StringUtils.isBlank(prefix) ? key : prefix + key;
    }

    private void checkCacheArgs(CacheEvict cacheEvict) {
        if (StringUtils.isBlank(cacheEvict.key())) {
            throw new RuntimeException("缓存key不能为空");
        }
    }
}
