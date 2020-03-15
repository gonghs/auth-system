package com.maple.common.enums;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * 枚举反序列化器 此类对@ResquestBody注解的类生效
 *
 * @author maple
 * @version 1.0
 * @since 2020-02-28 14:49
 */
public class EnumDeserializer implements ObjectDeserializer {
    @Override
    @SuppressWarnings("unchecked cast")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        final JSONLexer lexer = parser.lexer;
        final int token = lexer.token();
        Class cls = (Class) type;
        Object[] enumConstants = cls.getEnumConstants();
        if (BaseEnum.class.isAssignableFrom(cls)) {
            for (Object enumConstant : enumConstants) {
                BaseEnum baseEnum = ((BaseEnum) enumConstant);

                if (Objects.equals(baseEnum.getValue(), lexer.intValue()) || Objects.equals(baseEnum.getDesc(),
                        lexer.stringVal())) {
                    return (T) enumConstant;
                }
            }
        } else {
            //没实现EnumValue接口的 默认的按名字或者按ordinal
            if (token == JSONToken.LITERAL_INT) {
                int intValue = lexer.intValue();
                lexer.nextToken(JSONToken.COMMA);

                if (intValue < 0 || intValue > enumConstants.length) {
                    throw new JSONException("parse enum " + cls.getName() + " error, value : " + intValue);
                }
                return (T) enumConstants[intValue];
            } else if (token == JSONToken.LITERAL_STRING) {
                return (T) Enum.valueOf(cls, lexer.stringVal());
            }
        }
        return null;
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
