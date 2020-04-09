package com.maple.server.function.aspect;

import com.alibaba.fastjson.JSON;
import com.maple.server.common.anno.Cache;
import com.maple.server.utils.SpringElUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 缓存切面
 * 由于spring-cache不支持自定义ttl 因此采用自定义方式实现
 * 允许使用SpEl表达式设置key:
 * 例如取方法参数名为userContext的id字段为 '1' + #userContext.id 或 #{userContext.id}
 * 同时也支持使用下标方式获取,这种方式必须使用{}包裹表达式 例如取第一个参数的id字段为 #{[0].id}
 *
 * @author maple
 * @version 1.0
 * @since 2019-07-22 10:08
 */
@Aspect
@Component
public class CacheAspect {
    private final String prefix;
    private final StringRedisTemplate stringRedisTemplate;

    public CacheAspect(StringRedisTemplate stringRedisTemplate, @Value("${cache.prefix:}") String prefix) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.prefix = prefix;
    }

    @Around("@annotation(cache)")
    public Object cache(ProceedingJoinPoint joinPoint, Cache cache) throws Throwable {
        // 校验缓存key是否有效
        checkCacheArgs(cache);
        String key = (String) SpringElUtils.parseKey(cache.key(), ((MethodSignature) joinPoint.getSignature()).getMethod()
                , joinPoint.getArgs());
        if (StringUtils.isBlank(key)) {
            throw new RuntimeException("缓存key不能为空");
        }
        // 根据key,从缓存中尝试取值
        Object valueFromCache = getValueFromCache(joinPoint, joinKey(key));
        // 如果缓存有值,则直接返回
        if (!isNull(valueFromCache)) {
            return valueFromCache;
        }
        // 如果没有命名缓存，则调用方法，并且将返回值加入到缓存中
        return doInvokeMethodAndSetCache(joinPoint, cache, joinKey(key));
    }

    private String joinKey(String key) {
        return StringUtils.isBlank(prefix) ? key : prefix + key;
    }

    /**
     * 判断数据为空 从几个维度上判断 正常的空判断,Optional存在,List长度不为0
     *
     * @param o 源数据
     * @return boolean
     */
    private boolean isNull(Object o) {
        if (Objects.isNull(o)) {
            return true;
        }
        if (o instanceof Optional) {
            return !((Optional) o).isPresent();
        }
        if (o instanceof List) {
            return (((List) o)).isEmpty();
        }
        if (o instanceof Map) {
            return ((Map) o).isEmpty();
        }
        return false;
    }

    private void checkCacheArgs(Cache cache) {
        if (StringUtils.isBlank(cache.key())) {
            throw new RuntimeException("缓存key不能为空");
        }
        if (cache.ttlMinutes() <= 0) {
            throw new RuntimeException("缓存过期时间至少为1分钟");
        }
    }


    /**
     * 从缓存中取结果
     *
     * @param joinPoint 切面
     * @param key       key
     * @return 结果数据
     */
    private Object getValueFromCache(ProceedingJoinPoint joinPoint, String key) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        String cachedValue = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(cachedValue)) {
            return null;
        }
        return JSON.parseObject(cachedValue, method.getGenericReturnType());
    }

    /**
     * 执行方法，并且将返回值放到缓存
     *
     * @param joinPoint 切面数据
     * @param cache     注解信息
     * @param key       key
     * @return 执行结果
     */
    private Object doInvokeMethodAndSetCache(ProceedingJoinPoint joinPoint, Cache cache, String key) throws Throwable {
        Object returnValue = joinPoint.proceed();
        if (returnValue != null) {
            String json = JSON.toJSONString(returnValue);
            int ttlMinutes = cache.ttlMinutes();
            stringRedisTemplate.opsForValue().set(key, json, ttlMinutes, TimeUnit.MINUTES);
        }
        return returnValue;
    }
}
