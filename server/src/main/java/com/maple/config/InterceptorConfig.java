package com.maple.config;

import com.maple.common.enums.IntegerToEnumConverterFactory;
import com.maple.common.enums.StringToEnumConverterFactory;
import com.maple.function.interceptor.CurrentUserMethodArgumentResolver;
import com.maple.function.interceptor.JsonArgMethodArgumentResolver;
import com.maple.function.interceptor.LogInterceptor;
import com.maple.function.interceptor.PathInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 拦截器统一配置
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-20 16:23
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentUserMethodArgumentResolver());
        resolvers.add(jsonArgMethodArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor());
        registry.addInterceptor(pathInterceptor());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new IntegerToEnumConverterFactory());
        registry.addConverterFactory(new StringToEnumConverterFactory());
    }

    private CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver() {
        return new CurrentUserMethodArgumentResolver();
    }
    private JsonArgMethodArgumentResolver jsonArgMethodArgumentResolver() {
        return new JsonArgMethodArgumentResolver();
    }

    private LogInterceptor logInterceptor() {
        return new LogInterceptor();
    }

    private PathInterceptor pathInterceptor() {
        return new PathInterceptor();
    }
}
