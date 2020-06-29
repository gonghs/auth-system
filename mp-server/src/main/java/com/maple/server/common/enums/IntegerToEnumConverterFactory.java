package com.maple.server.common.enums;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * ConverterFactory 对表单提交形式的数据生效 序列化器
 * 根据Integer形式的码值或描述转化为枚举
 *
 * @author maple
 * @version 1.0
 * @since 2020-02-28 16:28
 */
public class IntegerToEnumConverterFactory implements ConverterFactory<Integer, BaseEnum> {
    private static final Map<Class<?>, Converter<Integer,? extends BaseEnum>> CONVERTER_MAP =  new HashMap<>();

    @NotNull
    @Override
    @SuppressWarnings("unchecked cast")
    public <T extends BaseEnum> Converter<Integer, T> getConverter(@NotNull Class<T> targetType) {
        Converter<Integer, T> converter = (Converter<Integer, T>) CONVERTER_MAP.get(targetType);
        if(converter == null) {
            converter = new IntegerToEnumConverter<>(targetType);
            CONVERTER_MAP.put(targetType, converter);
        }
        return converter;
    }

    static class IntegerToEnumConverter<T extends BaseEnum> implements Converter<Integer, T> {

        private final Map<Integer, T> enumMap = new HashMap<>();

        IntegerToEnumConverter(Class<T> enumType) {
            T[] enums = enumType.getEnumConstants();
            for (T e : enums) {
                enumMap.put(e.getValue(), e);
            }
        }

        @Override
        public T convert(@NotNull Integer source) {

            T t = enumMap.get(source);
            if (t == null) {
                // 异常可以稍后去捕获
                throw new IllegalArgumentException("No element matches " + source);
            }
            return t;
        }
    }
}
