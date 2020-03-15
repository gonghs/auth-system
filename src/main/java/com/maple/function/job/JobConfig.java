package com.maple.function.job;

import org.springframework.context.annotation.Configuration;

/**
 * 定时任务统一配置
 *
 * @author maple
 * @version 1.0
 * @since 2020-02-05 10:54
 */
@Configuration
public class JobConfig {
//    @Bean
//    public JobDetail myJobDetail(){
//        return JobBuilder.newJob(TestJob.class)
//                .withIdentity("myJob1","myJobGroup1")
//                //JobDataMap可以给任务execute传递参数
//                .usingJobData("job_param","job_param1")
//                .storeDurably()
//                .build();
//    }
//    @Bean
//    public Trigger myTrigger(){
//        return TriggerBuilder.newTrigger()
//                .forJob(myJobDetail())
//                .withIdentity("myTrigger1","myTriggerGroup1")
//                .usingJobData("job_trigger_param","job_trigger_param1")
//                .startNow()
//                //.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever())
//                .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ? 2020"))
//                .build();
//    }
}
