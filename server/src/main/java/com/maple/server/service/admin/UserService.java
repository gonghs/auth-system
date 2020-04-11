package com.maple.server.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.server.dto.admin.UserDTO;

import java.util.Optional;

/**
 * 用户服务
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-09 00:00
 */
public interface UserService extends IService<UserDTO> {
    /**
     * 根据账号查询对象
     *
     * @param account 账号
     * @return optional对象 可能为空
     */
    Optional<UserDTO> getByAccount(String account);

    /**
     * 测试
     *
     * @param account 账号
     */
    void test(String account);

    /**
     * 根据账号更新密码
     *
     * @param userDTO 账号和密码
     * @return 影响条目
     */
    boolean updateByAccount(UserDTO userDTO);

}
