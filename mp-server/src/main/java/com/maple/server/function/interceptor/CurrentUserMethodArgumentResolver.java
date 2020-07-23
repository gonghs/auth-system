package com.maple.server.function.interceptor;

import com.maple.server.common.anno.CurrentUser;
import com.maple.server.dto.admin.UserDTO;
import com.maple.server.service.admin.UserService;
import com.maple.starter.shiro.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


/**
 * 方法参数解析器
 *
 * @author maple
 * @version 1.0
 * @since 2019-07-16 15:20
 */
@AllArgsConstructor
public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().isAssignableFrom(UserDTO.class) && methodParameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal instanceof UserDTO) {
            return principal;
        }
        String userId = jwtUtils.getUserId();
        return userService.getById(userId);
    }
}
