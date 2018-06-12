package com.shiro.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/4 15:23
 */
public class LevelUtil {

    private final static String SEPARATOR = ".";
    public final static String ROOT = "0";


    // 0
    // 0.1
    // 0.2
    // 0.1.1
    // 0.1.2
    // 0.3
    public static String calculateLevel(String parentLevel, int parentId) {
        if (StringUtils.isBlank(parentLevel)) {
            return ROOT;
        } else {
            return StringUtils.join(parentLevel, SEPARATOR, parentId);
        }
    }




}
