package com.jaygoaler.pitools.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhanglj
 * @version 1.0.0
 * @date 2020/10/20 10:58
 * <p>
 * description: 整型数据(int)数据转中文工具
 */
@Slf4j
public class NumberFormate {

    private final static String[] source1 = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    private final static String[] source2 = {"", "十", "百", "千"};
    private final static String[] source3 = {"", "万", "亿"};

    /**
     * @param number
     * @return
     */
    public static String numberFormate(int number) {
        StringBuilder builder = new StringBuilder();
        String numberStr = String.valueOf(number);
        return getNumberString(builder, numberStr);
    }

    private static String getNumberString(StringBuilder builder, String numberStr) {
        List<String> numberStrs = new LinkedList<>();
        subStr(numberStrs, numberStr);
        List<String> strings = translationNumber(numberStrs);
        for (int i = strings.size() - 1; i >= 0; i--) {
            builder.append(strings.get(i)).append(source3[i]);
        }
        return builder.toString();
    }

    /**
     * 将对应数值转换为中文
     *
     * @param numberStrs
     * @return
     */
    private static List<String> translationNumber(List<String> numberStrs) {

        if (numberStrs == null || numberStrs.size() <= 0) {
            return numberStrs;
        }

//        // 如果数值小于99
//        if (numberStrs.size() == 1 && numberStrs.get(0).length() <= 2) {
//
//            return numberStrs.stream()
//                    .map(item -> {
//                        StringBuilder itemBuilder = new StringBuilder();
//
//                        char[] chars = item.toCharArray();
//
//                        // 如果数值在小于10
//                        if (chars.length < 2) {
//                            String s1 = String.valueOf(chars[0]);
//                            itemBuilder.append(source1[Integer.valueOf(s1)]);
//                        } else {
//                            String s1 = String.valueOf(chars[0]);
//                            s1 = source1[Integer.valueOf(s1)];
//
//                            String s2 = String.valueOf(chars[1]);
//                            s2 = source1[Integer.valueOf(s2)];
//
//                            // 如果数值在大于19
//                            if (!source1[1].equals(s1)) {
//                                itemBuilder.append(s1);
//                            }
//
//                            itemBuilder.append(source2[1]);
//                            itemBuilder.append(s2);
//
//                        }
//                        return itemBuilder.length() > 0 ? itemBuilder.toString() : null;
//                    })
//                    .filter(item -> !StringUtils.isEmpty(item))
//                    .map(item -> {
//                        // 去掉末尾多余的零
//                        while (item.length() > 1 && item.endsWith(source1[0])) {
//                            item = item.substring(0, item.length() - 1);
//                        }
//                        return item;
//                    })
//                    .collect(Collectors.toList());
//        }

        return numberStrs.stream()
                .map(item -> {
                    StringBuilder itemBuilder = new StringBuilder();

                    char[] chars = item.toCharArray();

                    for (int i = 0; i < chars.length; i++) {

                        String s1 = String.valueOf(chars[i]);

                        // 获取当前数值的中文
                        String s = source1[Integer.valueOf(s1)];

                        if (source1[0].equals(s) && itemBuilder.toString().endsWith(source1[0])) {
                            continue;
                        }

                        itemBuilder.append(s);

                        // 判断最后一位是否为零
                        if (!source1[0].equals(s) && i != chars.length) {
                            itemBuilder.append(source2[chars.length - (i + 1)]);
                        }
                    }
                    return itemBuilder.length() > 0 ? itemBuilder.toString() : null;
                })
                .filter(item -> !StringUtils.isEmpty(item))
                .map(item -> {
                    // 去掉末尾的零
                    while (item.endsWith(source1[0])) {
                        item = item.substring(0, item.length() - 1);
                    }
                    return item;
                })
                .collect(Collectors.toList());
    }

    /**
     * 将数值字符串从右到左按照4位一段截取
     *
     * @param numberStrs
     * @param numberStr
     */
    private static void subStr(List<String> numberStrs, String numberStr) {

        if (numberStr != null && numberStr.trim().length() > 0) {
            if (numberStr.length() > 4) {
                String substring = numberStr.substring(numberStr.length() - 4);
                String substring1 = numberStr.substring(0, numberStr.length() - 4);
                numberStrs.add(substring);
                subStr(numberStrs, substring1);
            } else {
                numberStrs.add(numberStr);
            }
        }
    }


    public static void main(String[] args) {
        String s = NumberFormate.numberFormate(110);
        log.info(s);
    }
}
