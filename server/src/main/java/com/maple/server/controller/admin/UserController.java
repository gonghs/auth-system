package com.maple.server.controller.admin;


import com.maple.server.common.builder.Results;
import com.maple.server.common.constant.SecurityConst;
import com.maple.server.common.entity.Result;
import com.maple.server.common.exception.AuthException;
import com.maple.server.controller.BaseController;
import com.maple.server.dto.admin.UserDTO;
import com.maple.server.service.admin.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author maple
 * @since 2019-09-09
 */
@Slf4j
@Controller
@RequestMapping("/user")
@Api(tags = "用户控制器")
public class UserController extends BaseController<UserService, UserDTO> {

    @SneakyThrows
    @PostMapping("checkLogin")
    @ApiOperation(value = "登陆校验", notes = "登陆校验")
    @ResponseBody
    public Result<Void> checkLogin(@Validated(UserDTO.Login.class) UserDTO userDTO) {
        //登陆
        Subject subject = SecurityUtils.getSubject();

        String account = userDTO.getAccount().trim();
        String password = userDTO.getPassword().trim();
        String host = request.getRemoteAddr();

        //获取token
        UsernamePasswordToken token = new UsernamePasswordToken(account, password, host);

        try {
            subject.login(token);
            // 登录成功
            UserDTO user = (UserDTO) subject.getPrincipal();

            request.setAttribute(SecurityConst.USER_ACCOUNT_SESSION_KEY, user.getAccount());
            log.info(user.getAccount() + "登陆成功");
            return Results.success();
        } catch (DisabledAccountException e) {
            throw new AuthException("账号异常!");
        } catch (AuthenticationException e) {
            throw new AuthException("账号或密码错误!");
        }
    }

    @GetMapping("manage")
    @ApiOperation(value = "用户管理页面", notes = "用户管理页面")
    public String manage() {
        return "admin/user-manage";
    }
}
