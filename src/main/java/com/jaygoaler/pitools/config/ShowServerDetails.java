package com.jaygoaler.pitools.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;

/**
 * @program: pitools
 * @description: 服务信息展示类
 * @author: JayGoal
 * @create: 2020-09-25 17:03
 **/
@Component
@Slf4j
public class ShowServerDetails {

    @Value("${server.port}")
    private String port;

    private String info = "{0}API文档地址:http://{1}:{2}/swagger-ui/index.html";

    private String getLocalHostInfo() {
        InetAddress localHost = null;
        try {
            localHost = Inet4Address.getLocalHost();
        } catch (UnknownHostException e) {
            log.error("error", e);
        }
        return localHost.getHostAddress();
    }

    @PostConstruct
    public void showDetail() {
        log.info(MessageFormat.format(info, "本地", "localhost", port));
        log.info(MessageFormat.format(info, "局域网", getLocalHostInfo(), port));
    }
}
