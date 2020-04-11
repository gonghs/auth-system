package com.maple.service.admin.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maple.dao.admin.UserRoleMapper;
import com.maple.dto.admin.UserRoleDTO;
import com.maple.service.admin.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色关联服务实现
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-09 00:04
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleDTO> implements UserRoleService {
    @Override
    public List<Long> getRoleIdByUserId(Long userId) {
        List<UserRoleDTO> userRoleList = lambdaQuery().select(UserRoleDTO::getRoleId).eq(UserRoleDTO::getUserId,
                userId).list();
        return userRoleList.stream().map(UserRoleDTO::getRoleId).collect(Collectors.toList());
    }
}
