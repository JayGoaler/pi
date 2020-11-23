package com.jaygoaler.pitools.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @program: backend
 * @description: 通用工具
 * @author: JayGoal
 * @create: 2020-09-29 11:13
 **/
@Slf4j
public class CommonUtils {

    /**
     * 获取request的ip地址
     * @param request request请求
     * @return ip地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress;
        String localhostipv4 = "127.0.0.1";
        String localhostipv6 = "0:0:0:0:0:0:0:1";
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (isaBoolean(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (isaBoolean(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (isaBoolean(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (localhostipv6.equals(ipAddress) || localhostipv4.equals(ipAddress)) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        log.error(e.getMessage());
                    }
                    assert inet != null;
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            // "***.***.***.***".length()
            int ipLength = 15;
            if (ipAddress != null && ipAddress.length() > ipLength) {
                // = 15
                String splitSymbol = ",";
                if (ipAddress.indexOf(splitSymbol) > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress="";
        }

        return ipAddress;
    }

    private static boolean isaBoolean(String ipAddress) {
        String unknownIp = "unknown";
        return ipAddress == null || ipAddress.length() == 0 || unknownIp.equalsIgnoreCase(ipAddress);
    }
}
