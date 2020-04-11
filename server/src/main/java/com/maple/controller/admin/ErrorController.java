package com.maple.controller.admin;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 错误页控制器
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-10 11:33
 */
@Controller
@RequestMapping("error")
@Api(tags = "错误页控制器")
public class ErrorController {

    @GetMapping("404")
    public String notFind() {
        return "error/404";
    }

    @GetMapping("500")
    public String error() {
        return "error/500";
    }
}
