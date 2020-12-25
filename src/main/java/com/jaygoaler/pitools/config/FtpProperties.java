package com.jaygoaler.pitools.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: pi
 * @description: ftp配置类
 * @author: JayGoal
 * @create: 2020-12-25 17:08
 **/
@Component
@Data
@ConfigurationProperties(prefix = "pi.ftp")
public class FtpProperties {
    private String host;
    private Integer port;
    private String username;
    private String password;
    private String basePath;
    private String httpPath;
}
