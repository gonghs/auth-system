package com.maple.server.common.enums;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * ConverterFactory 对表单提交形式的数据生效 序列化器
 * 根据字符串形式的码值或描述转化为枚举
 *
 * @author maple
 * @version 1.0
 * @since 2020-02-28 16:28
 */
public class StringToEnumConverterFactory implements ConverterFactory<String, BaseEnum> {
    private static final Map<Class<?>, Converter<String,? extends BaseEnum>> CONVERTER_MAP =  new HashMap<>();

    @NotNull
    @Override
    @SuppressWarnings("unchecked cast")
    public <T extends BaseEnum> Converter<String, T> getConverter(@NotNull Class<T> targetType) {
        Converter<String, T> converter = (Converter<String, T>) CONVERTER_MAP.get(targetType);
        if (converter == null) {
            converter = new StringToEnumConverter<>(targetType);
            CONVERTER_MAP.put(targetType, converter);
        }
        return converter;
    }

    static class StringToEnumConverter<T extends BaseEnum> implements Converter<String, T> {

        private final Map<String, T> enumMap = new HashMap<>();

        StringToEnumConverter(Class<T> enumType) {
            T[] enums = enumType.getEnumConstants();
            for (T e : enums) {
                enumMap.put(e.getDesc(), e);
                enumMap.put(Objects.toString(e.getValue()), e);
            }
        }

        @Override
        public T convert(@NotNull String source) {

            T t = enumMap.get(source);
            if (t == null) {
                // 异常可以稍后去捕获
                throw new IllegalArgumentException("No element matches " + source);
            }
            return t;
        }
    }
}
