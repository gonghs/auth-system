package com.maple.server.controller.admin;

import com.maple.server.common.anno.CurrentUser;
import com.maple.server.dto.admin.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 主控制器
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-10 11:33
 */
@Controller
public class IndexController {
    @GetMapping(value = {"/", "home"})
    public String home(Model model, @CurrentUser UserDTO userDTO) {
        model.addAttribute("userName", userDTO.getUserName());
        return "home";
    }

    @GetMapping("firstPage")
    public String firstPage() {
        return "firstPage";
    }
}
