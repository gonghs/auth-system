package com.maple.server.utils;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.loader.api.BeanMappingBuilder;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.dozermapper.core.loader.api.TypeMappingOptions.*;

/**
 * bean工具类
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-07 10:24
 */
@UtilityClass
public class DozerUtils {
    /**
     * 普通转换器
     */
    private static Mapper mapper;


    static {
        mapper = DozerBeanMapperBuilder.buildDefault();
    }

    /**
     * 单个对象的深度复制及类型转换
     *
     * @param s   数据对象
     * @param clz 复制目标类型
     * @param <T> 目标类型
     * @param <S> 源类型
     * @return 转换后的实体
     */
    public <T, S> T convert(S s, Class<T> clz) {
        if (s == null) {
            return null;
        }
        return mapper.map(s, clz);
    }

    /**
     * 单个对象的深度复制及类型转换(忽略异常)
     *
     * @param s   数据对象
     * @param clz 复制目标类型
     * @param <T> 目标类型
     * @param <S> 源类型
     * @return 转换后的实体
     */
    public <T, S> T convertIgnoreError(S s, Class<T> clz) {
        DozerBeanMapperBuilder dozerBeanMapperBuilder = DozerBeanMapperBuilder.create();
        Mapper mapper = dozerBeanMapperBuilder.withMappingBuilders(new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(s.getClass(), clz, stopOnErrors(false));
            }
        }).build();
        if (s == null) {
            return null;
        }
        return mapper.map(s, clz);
    }

    /**
     * list深度复制
     *
     * @param s   数据对象
     * @param clz 复制目标类型
     * @param <T> 目标类型
     * @param <S> 源类型
     * @return 拷贝list
     */
    public <T, S> List<T> convert(List<S> s, Class<T> clz) {
        if (s == null) {
            return null;
        }
        List<T> list = new ArrayList<>();
        for (S vs : s) {
            list.add(mapper.map(vs, clz));
        }
        return list;
    }

    /**
     * Set深度复制
     *
     * @param s   数据对象
     * @param clz 复制目标类型
     * @param <T> 拷贝类型
     * @param <S> 源类型
     * @return 拷贝set
     */
    public <T, S> Set<T> convert(Set<S> s, Class<T> clz) {
        if (s == null) {
            return null;
        }
        Set<T> set = new HashSet<>();
        for (S vs : s) {
            set.add(mapper.map(vs, clz));
        }
        return set;
    }

    /**
     * 数组深度复制
     *
     * @param s   数据对象
     * @param clz 复制目标类型
     * @param <T> 拷贝类型
     * @param <S> 源类型
     * @return 拷贝后的数组
     */
    public <T, S> T[] convert(S[] s, Class<T> clz) {
        if (s == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) Array.newInstance(clz, s.length);
        for (int i = 0; i < s.length; i++) {
            arr[i] = mapper.map(s[i], clz);
        }
        return arr;
    }

    /**
     * 拷贝非空且非空串属性
     *
     * @param source 数据源
     * @param target 指向源
     */
    public void copyIgnoreNullAndBlank(Object source, Object target) {
        DozerBeanMapperBuilder dozerBeanMapperBuilder = DozerBeanMapperBuilder.create();
        Mapper mapper = dozerBeanMapperBuilder.withMappingBuilders(new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(source.getClass(), target.getClass(), mapNull(false), mapEmptyString(false));
            }
        }).build();
        mapper.map(source, target);
    }

    /**
     * 拷贝非空属性
     *
     * @param source 数据源
     * @param target 指向源
     */
    public void copyIgnoreNull(Object source, Object target) {
        DozerBeanMapperBuilder dozerBeanMapperBuilder = DozerBeanMapperBuilder.create();
        Mapper mapper = dozerBeanMapperBuilder.withMappingBuilders(new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(source.getClass(), target.getClass(), mapNull(false));
            }
        }).build();
        mapper.map(source, target);
    }
}
