package com.maple.dao.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maple.dto.admin.RoleMenuDTO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据层
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-07 19:17
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenuDTO> {


}
