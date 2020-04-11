package com.maple.server.function.job;

import com.maple.server.dto.admin.UserDTO;
import com.maple.server.service.admin.UserService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

/**
 * 测试定时任务
 *
 * @author maple
 * @version 1.0
 * @since 2020-02-05 10:51
 */
public class TestJob extends QuartzJobBean {
    @Autowired
    private UserService userService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<UserDTO> list = userService.lambdaQuery().list();
        System.out.println("hello------------------------");
        System.out.println(list);
    }
}
