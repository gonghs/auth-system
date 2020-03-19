package com.maple.utils;

import lombok.experimental.UtilityClass;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * spEL表达式解析工具
 *
 * @author maple
 * @version 1.0
 * @since 2019-07-19 09:55
 */
@UtilityClass
public class SpringElUtils {
    /**
     * 转换参数为字符串
     *
     * @param spEL       spEL表达式
     * @param contextObj 上下文对象
     * @return 解析的字符串值
     */
    public Object parse(String spEL, Method method, Object[] contextObj) {
        LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(spEL);
        String[] params = discoverer.getParameterNames(method);
        StandardEvaluationContext context = new StandardEvaluationContext();
        if (params != null) {
            for (int len = 0; len < params.length; len++) {
                context.setVariable(params[len], contextObj[len]);
            }
        }
        return exp.getValue(context);
    }

    /**
     * 校验并返回实际使用的el表达式
     *
     * @param spEL el表达式
     * @return 实际使用的el表达式
     */
    public Object parseKey(String spEL, Method method, Object[] contextObj) {
        // 如果不是SpEL表达式，则直接返回
        if (!spEL.contains("#")) {
            return spEL;
        }
        return parse(spEL, method, contextObj);
    }
}
