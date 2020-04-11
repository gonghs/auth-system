package com.maple.server.tool;

import com.maple.server.BaseTest;
import com.maple.server.dto.admin.MenuDTO;
import com.maple.server.service.admin.MenuService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 菜单组装工具测试
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-16 23:03
 */
public class MenuToolTest extends BaseTest {
    @Autowired
    private MenuService menuService;

    @Test
    public void assemblyMenu() {
        List<MenuDTO> sourceMenuList = menuService.list();
        List<MenuDTO> assemblyList = MenuTool.assemblyMenu(sourceMenuList);
        System.out.println(assemblyList);
    }
}
