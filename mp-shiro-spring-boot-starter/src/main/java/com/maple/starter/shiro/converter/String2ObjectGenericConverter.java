package com.maple.starter.shiro.converter;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import javax.servlet.Filter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author maple
 */
@Slf4j
public class String2ObjectGenericConverter implements GenericConverter {

    private static final Set<ConvertiblePair> CONVERTIBLE_TYPES;

    static {
        Set<ConvertiblePair> convertiblePairs = new HashSet<>();
        convertiblePairs.add(new ConvertiblePair(String.class, CredentialsMatcher.class));
        convertiblePairs.add(new ConvertiblePair(String.class, Filter.class));
        convertiblePairs.add(new ConvertiblePair(String.class, Realm.class));
        convertiblePairs.add(new ConvertiblePair(String.class, byte[].class));
        CONVERTIBLE_TYPES = Collections.unmodifiableSet(convertiblePairs);
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return CONVERTIBLE_TYPES;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        try {
            if (targetType.getObjectType().isAssignableFrom(byte[].class)) {
                return source.toString().getBytes();
            }
            return BeanUtils.instantiateClass(Class.forName(ObjectUtil.toString(source)));
        } catch (ClassNotFoundException e) {
            log.error("can't convert {} from {} to {}", source, sourceType.getName(), targetType.getName(), e);
        }
        return null;
    }
}
