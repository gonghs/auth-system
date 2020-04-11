package com.maple.server.service.admin.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maple.server.dao.admin.RoleMenuMapper;
import com.maple.server.dto.admin.RoleMenuDTO;
import com.maple.server.service.admin.RoleMenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色菜单关联服务实现
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-09 00:04
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenuDTO> implements RoleMenuService {

    @Override
    public List<Long> getMenuIdInRoleId(List<Long> roleIdList) {
        // 查询 转化并去重
        return lambdaQuery().select(RoleMenuDTO::getMenuId).in(RoleMenuDTO::getRoleId, roleIdList).list()
                .stream().map(RoleMenuDTO::getMenuId).distinct().collect(Collectors.toList());
    }
}
