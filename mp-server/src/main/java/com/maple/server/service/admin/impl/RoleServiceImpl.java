package com.maple.server.service.admin.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maple.server.dao.admin.RoleMapper;
import com.maple.server.dto.admin.RoleDTO;
import com.maple.server.service.admin.RoleService;
import org.springframework.stereotype.Service;

/**
 * 角色服务实现
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-09 00:04
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleDTO> implements RoleService {
}
