package com.maple.utils;

import cn.hutool.core.collection.CollUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis 工具类
 *
 * @author gonghs
 * @version 1.0
 * @since 2020-04-01 11:34
 */
@Component
@Validated
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
    public void set(@NotBlank String key, Object object) {
        redisTemplate.opsForValue().set(joinKey(key), object);
    }


    /**
     * 设置缓存
     *
     * @param key        缓存key
     * @param object     缓存对象
     * @param ttlMinutes 过期时间
     */
    public void set(@NotBlank String key, Object object, long ttlMinutes) {
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
    public void set(@NotBlank String key, Object object, long ttl, TimeUnit timeUnit) {
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
    public <T> T get(@NotBlank String key) {
        return (T) getAny(key);
    }

    /**
     * 获取缓存对象
     *
     * @param key 键
     * @return any 值对象
     */
    private Object getAny(@NotBlank String key) {
        checkKeyNotBlank(key);
        return redisTemplate.opsForValue().get(joinKey(key));
    }

    /**
     * 删除key
     *
     * @param key key
     */
    public Boolean del(@NotBlank String key) {
        checkKeyNotBlank(key);

        return redisTemplate.delete(joinKey(key));
    }

    /**
     * 判断key是否存在
     *
     * @param key key
     * @return 是否存在 可能为空
     */
    public Boolean hasKey(@NotBlank String key) {
        checkKeyNotBlank(key);

        return redisTemplate.hasKey(joinKey(key));
    }

    /**
     * 放置一个键值对
     *
     * @param key     hash键
     * @param hashKey hash key
     * @param value   map值
     */
    public void put(@NotBlank String key, @NotBlank String hashKey, Object value) {
        checkKeyNotBlank(key);

        redisTemplate.opsForHash().put(joinKey(key), hashKey, value);
    }

    /**
     * 放置一个键值对 并设置过期时间
     *
     * @param key     hash键
     * @param hashKey hash key
     * @param value   map值
     */
    public void put(@NotBlank String key, @NotBlank String hashKey, Object value, long ttlMinutes) {
        put(key, hashKey, value, ttlMinutes, DEFAULT_TIME_UNIT);
    }

    /**
     * 放置一个键值对 并设置过期时间
     *
     * @param key     hash键
     * @param hashKey hash key
     * @param value   map值
     */
    public void put(@NotBlank String key, @NotBlank String hashKey, Object value, long ttl, TimeUnit timeUnit) {
        put(key, hashKey, value);
        // 设置过期时间
        expire(key, ttl, timeUnit);
    }

    /**
     * 从hash中获取对象
     *
     * @param key     key
     * @param hashKey hash key
     * @param <T>     T
     * @return T
     */
    @SuppressWarnings("unchecked cast")
    public <T> T get(@NotBlank String key, @NotBlank String hashKey) {
        checkKeyNotBlank(key, hashKey);

        return (T) redisTemplate.opsForHash().get(joinKey(key), hashKey);
    }

    /**
     * 获取hash长度
     *
     * @param key key
     * @return size
     */
    public Long hashSize(@NotBlank String key) {
        checkKeyNotBlank(key);

        return redisTemplate.opsForHash().size(joinKey(key));
    }

    /**
     * 获取hash 所有key
     *
     * @param key key
     * @return size
     */
    public Set<Object> hashKeys(@NotBlank String key) {
        checkKeyNotBlank(key);

        Set<Object> keys = redisTemplate.opsForHash().keys(joinKey(key));
        return CollectionUtils.isEmpty(keys) ? CollUtil.newHashSet() : keys;
    }

    /**
     * 获取hash 所有值
     *
     * @param key key
     * @return size
     */
    public List<Object> hashValues(@NotBlank String key) {
        checkKeyNotBlank(key);

        List<Object> keys = redisTemplate.opsForHash().values(joinKey(key));
        return CollectionUtils.isEmpty(keys) ? CollUtil.newArrayList() : keys;
    }

    /**
     * 设置key过期时间
     *
     * @param key      key
     * @param ttl      过期时间
     * @param timeUnit 时间单位
     */
    public Boolean expire(@NotBlank String key, long ttl, TimeUnit timeUnit) {
        checkKeyNotBlank(key);

        return redisTemplate.expire(joinKey(key), ttl, timeUnit);
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
