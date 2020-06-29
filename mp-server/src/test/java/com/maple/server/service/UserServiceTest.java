package com.maple.server.service;

import com.maple.server.BaseTest;
import com.maple.server.dto.admin.UserDTO;
import com.maple.server.service.admin.UserService;
import com.maple.server.tool.column.utils.ColumnContextHolder;
import com.maple.server.utils.DigestUtils;
import org.junit.Test;

import javax.inject.Inject;

/**
 * 用户服务类测试
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-10 16:03
 */
public class UserServiceTest extends BaseTest {
    @Inject
    private UserService userService;

    @Test
    public void get() {
        ColumnContextHolder.setColumnValue("1");
        System.out.println(userService.getByAccount("maple"));
    }

    @Test
    public void get1() {
        userService.test("maple");
    }

    @Test
    public void updateByAccount() {
        UserDTO userDTO = new UserDTO();
        userDTO.setAccount("maple");
        String password = DigestUtils.md5("maple", "1");
        System.out.println(password);
        userDTO.setPassword(password);
        userService.updateByAccount(userDTO);
    }

    @Test
    public void delete() {
        userService.lambdaUpdate().eq(UserDTO::getAccount, "test").remove();
    }
}
