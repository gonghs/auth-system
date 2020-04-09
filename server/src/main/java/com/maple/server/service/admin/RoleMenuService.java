package com.maple.server.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.server.dto.admin.RoleMenuDTO;

import java.util.List;

/**
 * 角色菜单关联服务
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-09 00:00
 */
public interface RoleMenuService extends IService<RoleMenuDTO> {
    /**
     * 根据角色id获取菜单id列表
     *
     * @param roleIdList roleId 列表
     * @return menuId列表
     */
    List<Long> getMenuIdInRoleId(List<Long> roleIdList);
}
