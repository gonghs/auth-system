package com.maple.server.service.admin.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maple.server.dao.admin.UserMapper;
import com.maple.server.dto.admin.UserDTO;
import com.maple.server.service.admin.UserService;
import com.maple.server.utils.SpringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 用户服务实现
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-09 00:04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDTO> implements UserService {

    @Override
//    @Cache(key = "#account",ttlMinutes = 10)
    public Optional<UserDTO> getByAccount(String account) {
        return Optional.ofNullable(lambdaQuery().eq(UserDTO::getAccount, account).one());
    }

    @Override
    public void test(String account) {
        Optional<UserDTO> byAccount = SpringUtils.getBean(this.getClass()).getByAccount(account);
        System.out.println(byAccount.orElse(null));
    }

    @Override
    public boolean updateByAccount(UserDTO userDTO) {
        return lambdaUpdate().eq(UserDTO::getAccount, userDTO.getAccount()).update(userDTO);
    }
}
