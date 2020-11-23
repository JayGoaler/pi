package com.jaygoaler.pitools.utils;

import java.util.UUID;

/**
 * @Author: 杨暾
 * @Date: 2019/7/20 14:08
 * @Description: UUID套件
 */
public class UUIDUtil {
    /**
     * 创建UUID
     * @return UUID
     */
    public static String createUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
