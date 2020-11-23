package com.jaygoaler.pitools.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author zhanglj
 * @version 1.0.0
 * @date 2020/7/16 13:45
 * <p>
 * description: 对象反射操作工具类
 */
@Slf4j
public class ObjectTools {

    /**
     * 为指定对象的指定属性赋值
     *
     * @param object
     * @param attributesName
     * @param attributesValue
     */
    public static void setObjectValue(Object object, String attributesName, Object attributesValue) {
        if (Objects.isNull(object)) {
            throw new NullPointerException("对象为空" + object);
        }
        Method[] methods = object.getClass().getMethods();

        if (Objects.isNull(methods) || methods.length <= 0) {
            throw new NullPointerException("对象没有get/set方法：" + object.getClass().getName());
        }
        for (Method method : methods) {
            // 判断挡墙方法是否是指定属性的set方法
            if (method.getName().startsWith("set") && method.getName().toLowerCase().contains(attributesName)) {
                try {
                    method.invoke(object, attributesValue);
                } catch (IllegalAccessException e) {
                    log.error("error", e);
                } catch (InvocationTargetException e) {
                    log.error("error", e);
                }
            }
        }
    }

    /**
     * 获取指定对象的指定属性值
     *
     * @param object
     * @param column
     * @return
     */
    public static Object getObjectValue(Object object, String column) {
        if (Objects.isNull(object)) {
            throw new NullPointerException("对象为空" + object);
        }
        Method[] methods = object.getClass().getMethods();
        if (Objects.isNull(methods) || methods.length <= 0) {
            throw new NullPointerException("对象没有get/set方法：" + object.getClass().getName());
        }
        for (Method method : methods) {
            // 判断挡墙方法是否是指定属性的set方法
            if (method.getName().startsWith("get") && method.getName().toLowerCase().contains(column)) {
                try {
                    Object invoke = method.invoke(object);
                    if (Objects.isNull(invoke)) {
                        return null;
                    }
                    return invoke;
                } catch (Exception e) {
                    log.error("error", e);
                    return null;
                }
            }
        }
        return null;
    }

}
