package com.maple.function.basic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.maple.common.constant.GlobalConst;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;


/**
 * 实现redis序列化和反序列化
 *
 * @author maple
 * @version V1.0
 * @since 2020-3-31
 */
public class FastJsonRedisSerializer<T> implements RedisSerializer<T> {
    private Class<T> clazz;

    public FastJsonRedisSerializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(GlobalConst.DEFAULT_CHAERSET);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (ArrayUtils.isEmpty(bytes)) {
            return null;
        }
        String str = new String(bytes, GlobalConst.DEFAULT_CHAERSET);
        return JSON.parseObject(str, clazz);
    }
}