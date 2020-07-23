package com.maple.server.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.maple.common.builder.Results;
import com.maple.common.entity.Result;
import com.maple.server.dto.admin.UserDTO;
import com.maple.server.service.admin.UserService;
import com.maple.starter.shiro.exception.AuthException;
import com.maple.starter.shiro.utils.JwtUtils;
import com.maple.starter.shiro.utils.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * token控制器
 *
 * @author maple
 * @version 1.0
 * @since 2020-06-30 15:05
 */
@Api(tags = "token控制器")
@RestController
@RequestMapping("token")
public class TokenController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping
    @ApiOperation(value = "获取token", notes = "获取token")
    public Result<String> getToken(@Validated(UserDTO.Login.class) UserDTO userDTO) {
        Optional<UserDTO> userOpt = userService.getByAccount(userDTO.getAccount());
        if (userOpt.isPresent()) {
            UserDTO dbUser = userOpt.get();
            if (StrUtil.equalsIgnoreCase(dbUser.getPassword(), ShiroUtils.genMd5Password(userDTO.getPassword(),
                    userDTO.getAccount(), 2))) {
                return Results.success(jwtUtils.sign(dbUser.getId().toString()));
            }
        }
        throw new AuthException("用户校验失败!");
    }
}
