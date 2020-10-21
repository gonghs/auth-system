package com.maple.server.controller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 测试控制器
 *
 * @author maple
 * @version 1.0
 * @since 2020-10-20 23:26
 */
@Controller
@RequestMapping("test")
@Api(tags = "测试控制器")
public class TestController {
    @GetMapping("test")
    public String test() {
        return "test";
    }
}
