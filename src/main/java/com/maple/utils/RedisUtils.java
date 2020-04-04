package com.maple.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * redis 工具类
 *
 * @author gonghs
 * @version 1.0
 * @since 2020-04-01 11:34
 */
@Component
public class RedisUtils {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Value("${cache.prefix:}")
    private String prefix;
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MINUTES;

    /**
     * 设置缓存 无过期时间
     *
     * @param key    缓存key
     * @param object 缓存对象
     */
    public void set(String key, Object object) {
        redisTemplate.opsForValue().set(joinKey(key), object);
    }


    /**
     * 设置缓存
     *
     * @param key        缓存key
     * @param object     缓存对象
     * @param ttlMinutes 过期时间
     */
    public void set(String key, Object object, long ttlMinutes) {
        set(key, object, ttlMinutes, DEFAULT_TIME_UNIT);
    }


    /**
     * 设置缓存
     *
     * @param key      缓存key
     * @param object   缓存对象
     * @param ttl      过期时间
     * @param timeUnit 时间单位
     */
    public void set(String key, Object object, long ttl, TimeUnit timeUnit) {
        checkKeyNotBlank(key);

        redisTemplate.opsForValue().set(joinKey(key), object, ttl, timeUnit);
    }


    /**
     * 获取指定类型缓存对象
     *
     * @param key 键
     * @return any 值对象
     */
    @SuppressWarnings("unchecked cast")
    public <T> T get(String key) {
        return (T) getAny(key);
    }

    /**
     * 获取缓存对象
     *
     * @param key 键
     * @return any 值对象
     */
    private Object getAny(String key) {
        checkKeyNotBlank(key);
        return redisTemplate.opsForValue().get(joinKey(key));
    }

    /**
     * 删除key
     *
     * @param key key
     */
    public Boolean del(String key) {
        checkKeyNotBlank(key);

        return redisTemplate.delete(joinKey(key));
    }

    private static void checkKeyNotBlank(String... key) {
        for (String s : key) {
            if (StringUtils.isBlank(s)) {
                throw new IllegalArgumentException("key不能为空");
            }
        }
    }

    private String joinKey(String key) {
        return StringUtils.isBlank(prefix) ? key : prefix + key;
    }
}
