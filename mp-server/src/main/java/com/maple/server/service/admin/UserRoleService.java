package com.maple.server.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.server.dto.admin.UserRoleDTO;

import java.util.List;

/**
 * 用户角色关联服务
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-09 00:00
 */
public interface UserRoleService extends IService<UserRoleDTO> {
    /**
     * 根据用户id获取角色id
     *
     * @param userId userId
     * @return roleId列表
     */
    List<Long> getRoleIdByUserId(Long userId);
}
