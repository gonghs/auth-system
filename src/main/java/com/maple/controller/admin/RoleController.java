package com.maple.controller.admin;


import com.maple.common.builder.Results;
import com.maple.common.entity.Result;
import com.maple.controller.BaseController;
import com.maple.dto.admin.RoleDTO;
import com.maple.service.admin.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/role")
@Api(tags = "角色控制器")
public class RoleController extends BaseController<RoleService, RoleDTO> {

    @GetMapping("manage")
    @ApiOperation(value = "角色管理页面", notes = "角色管理页面")
    public String manage() {
        return "admin/role-manage";
    }

    @PostMapping("save")
    @ApiOperation(value = "新增角色", notes = "新增角色")
    @ResponseBody
    public Result<RoleDTO> save(@RequestBody RoleDTO roleDTO) {
        service.save(roleDTO);
        return Results.success(roleDTO);
    }

    @PostMapping("update")
    @ApiOperation(value = "更新角色", notes = "更新角色")
    @ResponseBody
    public Result<RoleDTO> update(@RequestBody RoleDTO roleDTO) {
        service.lambdaUpdate().eq(RoleDTO::getId, roleDTO.getId()).update(roleDTO);
        return Results.success(roleDTO);
    }
}
