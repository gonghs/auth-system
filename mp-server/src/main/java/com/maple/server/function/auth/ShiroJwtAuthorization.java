package com.maple.server.function.auth;

import com.maple.server.dto.admin.UserDTO;
import com.maple.server.service.admin.UserService;
import com.maple.starter.shiro.jwt.DefaultShiroJwtAuthorization;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 覆盖权限处理
 *
 * @author maple
 * @version 1.0
 * @since 2020-06-30 15:13
 */
@Slf4j
@Component
public class ShiroJwtAuthorization extends DefaultShiroJwtAuthorization {
    @Autowired
    private UserService userService;

    @Override
    public SimpleAuthorizationInfo getAuthorizationInfo(String userId) {
        UserDTO userDTO = userService.getById(userId);
        log.info("用户登录认证！用户信息user：" + userDTO);
        if (Objects.isNull(userDTO)) {
            return null;
        }
        return new SimpleAuthorizationInfo();
    }
}
