package com.maple.utils;

import cn.hutool.core.collection.CollUtil;
import com.maple.common.anno.Prefix;
import org.apache.commons.collections4.CollectionUtils;
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
 * @author maple
 * @version 1.0
 * @since 2020-04-01 11:34
 */
@Component
@Validated
public class RedisUtils {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PREFIX_ENV = "cache.prefix";
    private static final String PREFIX_KEY = "key";
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MINUTES;

    public RedisUtils(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 设置缓存 无过期时间
     *
     * @param key    缓存key
     * @param object 缓存对象
     */
    @Prefix(value = PREFIX_KEY, prefix = PREFIX_ENV)
    public void set(@NotBlank String key, Object object) {
        redisTemplate.opsForValue().set(key, object);
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
    @Prefix(value = PREFIX_KEY, prefix = PREFIX_ENV)
    public void set(@NotBlank String key, Object object, long ttl, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, object, ttl, timeUnit);
    }


    /**
     * 获取指定类型缓存对象
     *
     * @param key 键
     * @return any 值对象
     */
    @SuppressWarnings("unchecked cast")
    @Prefix(value = PREFIX_KEY, prefix = PREFIX_ENV)
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
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除key
     *
     * @param key key
     */
    @Prefix(value = PREFIX_KEY, prefix = PREFIX_ENV)
    public Boolean del(@NotBlank String key) {

        return redisTemplate.delete(key);
    }

    /**
     * 判断key是否存在
     *
     * @param key key
     * @return 是否存在 可能为空
     */
    @Prefix(value = PREFIX_KEY, prefix = PREFIX_ENV)
    public Boolean hasKey(String key) {

        return redisTemplate.hasKey(key);
    }

    /**
     * 放置一个键值对
     *
     * @param key     hash键
     * @param hashKey hash key
     * @param value   map值
     */
    @Prefix(value = PREFIX_KEY, prefix = PREFIX_ENV)
    public void put(@NotBlank String key, @NotBlank String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
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
    @Prefix(value = PREFIX_KEY, prefix = PREFIX_ENV)
    public <T> T get(@NotBlank String key, @NotBlank String hashKey) {
        return (T) redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 获取hash长度
     *
     * @param key key
     * @return size
     */
    @Prefix(value = PREFIX_KEY, prefix = PREFIX_ENV)
    public Long hashSize(@NotBlank String key) {
        return redisTemplate.opsForHash().size(key);
    }

    /**
     * 获取hash 所有key
     *
     * @param key key
     * @return size
     */
    @Prefix(value = PREFIX_KEY, prefix = PREFIX_ENV)
    public Set<Object> hashKeys(@NotBlank String key) {
        Set<Object> keys = redisTemplate.opsForHash().keys(key);
        return CollectionUtils.isEmpty(keys) ? CollUtil.newHashSet() : keys;
    }

    /**
     * 获取hash 所有值
     *
     * @param key key
     * @return size
     */
    @Prefix(value = PREFIX_KEY, prefix = PREFIX_ENV)
    public List<Object> hashValues(@NotBlank String key) {
        List<Object> keys = redisTemplate.opsForHash().values(key);
        return CollectionUtils.isEmpty(keys) ? CollUtil.newArrayList() : keys;
    }

    /**
     * 设置key过期时间
     *
     * @param key      key
     * @param ttl      过期时间
     * @param timeUnit 时间单位
     */
    @Prefix(value = PREFIX_KEY, prefix = PREFIX_ENV)
    public Boolean expire(@NotBlank String key, long ttl, TimeUnit timeUnit) {
        return redisTemplate.expire(key, ttl, timeUnit);
    }
}
