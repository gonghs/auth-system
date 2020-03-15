package com.maple.controller.admin;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 登陆控制器
 *
 * @author gonghs
 * @version 1.0
 * @since 2019-09-10 11:29
 */
@Controller
@Slf4j
@Api(tags = "登陆控制器")
public class LoginController {
    @GetMapping(value = {"login"})
    public String login() {
        // TODO 被踢出时通知用户
        return "login";
    }

    @GetMapping("logout")
    public String logout() {
        log.info("退出系统  并重定向至登陆页 ");
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:login";
    }
}
