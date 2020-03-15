package com.maple.controller.admin;


import com.maple.common.anno.CurrentUser;
import com.maple.common.builder.Results;
import com.maple.common.entity.Result;
import com.maple.controller.BaseController;
import com.maple.dto.admin.MenuDTO;
import com.maple.dto.admin.UserDTO;
import com.maple.service.admin.MenuService;
import com.maple.service.admin.RoleMenuService;
import com.maple.service.admin.UserRoleService;
import com.maple.tool.MenuTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author maple
 * @since 2019-09-09
 */
@RestController
@RequestMapping("/menu")
@Slf4j
@Api(tags = "菜单控制器")
public class MenuController extends BaseController<MenuService, MenuDTO> {
    private final RoleMenuService roleMenuService;
    private final UserRoleService userRoleService;

    public MenuController(RoleMenuService roleMenuService,
                          UserRoleService userRoleService) {
        this.roleMenuService = roleMenuService;
        this.userRoleService = userRoleService;
    }

    @GetMapping("getList")
    @ApiOperation(value = "获取菜单列表", notes = "获取菜单列表")
    public Result<List<MenuDTO>> getMenuList(@CurrentUser UserDTO user) {
        List<MenuDTO> defaultList = new ArrayList<>();
        // 查询用户角色列表
        List<Long> roleIdList = userRoleService.getRoleIdByUserId(user.getId());
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Results.success(defaultList);
        }
        // 查询角色菜单列表
        List<Long> menuIdList = roleMenuService.getMenuIdInRoleId(roleIdList);
        if (CollectionUtils.isEmpty(menuIdList)) {
            return Results.success(defaultList);
        }
        List<MenuDTO> menuList = service.lambdaQuery().in(MenuDTO::getId, menuIdList).list();
        // 组装菜单用于前端渲染
        return Results.success(MenuTool.assemblyMenu(menuList));
    }
}
