package com.maple.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * kano配置文件
 *
 * @author gonghs
 */
@Configuration
@ConfigurationProperties(prefix = "xxl.job")
@Data
public class XxlJobProperties {
    private String accessToken;

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "xxl.job.admin")
    public static class XxlJobAdminProperties {
        private String addresses;
    }

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "xxl.job.executor")
    public static class XxlJobExecutorProperties {
        private String appName;
        private String ip;
        private Integer port;
        private String logPath;
        private Integer logRetentionDays;
    }


}
