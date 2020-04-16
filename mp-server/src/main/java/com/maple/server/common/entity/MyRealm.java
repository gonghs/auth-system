package com.maple.server.common.entity;

import com.maple.server.dto.admin.UserDTO;
import com.maple.server.service.admin.MenuService;
import com.maple.server.service.admin.RoleService;
import com.maple.server.service.admin.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * MyRealm
 *
 * @author : maple
 * @version : 1.0
 * @date : 2018/11/22 15:53
 */
@Slf4j
public class MyRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;


    /**
     * 功能描述: 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
        //授权
        log.info("授予角色和权限");
        // 添加权限 和 角色信息
        return new SimpleAuthorizationInfo();
    }


    /**
     * 认证
     *
     * @param authenticationToken token
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        log.info("用户登录认证：验证当前Subject时获取到token为：" + ReflectionToStringBuilder
                .toString(token, ToStringStyle.MULTI_LINE_STYLE));
        String account = token.getUsername();
        // 调用数据层
        Optional<UserDTO> userOpt = userService.getByAccount(account);
        log.info("用户登录认证！用户信息user：" + userOpt);
        if (!userOpt.isPresent()) {
            return null;
        }
        UserDTO userDTO = userOpt.get();
        // 返回密码
        return new SimpleAuthenticationInfo(userDTO, userDTO.getPassword(), ByteSource.Util.bytes(account),
                getName());

    }

}
