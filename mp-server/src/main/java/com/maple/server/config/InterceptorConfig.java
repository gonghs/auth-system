package com.maple.server.config;

import com.maple.server.common.enums.IntegerToEnumConverterFactory;
import com.maple.server.common.enums.StringToEnumConverterFactory;
import com.maple.server.function.interceptor.CurrentUserMethodArgumentResolver;
import com.maple.server.function.interceptor.JsonArgMethodArgumentResolver;
import com.maple.server.function.interceptor.LogInterceptor;
import com.maple.server.function.interceptor.PathInterceptor;
import com.maple.server.service.admin.UserService;
import com.maple.starter.shiro.utils.JwtUtils;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {
    private final JwtUtils jwtUtils;
    private final UserService userService;
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
        return new CurrentUserMethodArgumentResolver(jwtUtils, userService);
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
