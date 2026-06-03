package com.geo.dsp.common.util;

import java.util.UUID;

/**
 * UUID生成与校验工具类
 * 统一项目UUID生成规则
 */
public class UuidUtil {
    /** 生成32位无横线UUID（推荐） */
    public static String generateUuid() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    /** 校验UUID格式 */
    public static boolean isValidUuid(String uuid) {
        if (uuid == null) return false;
        String regex = "^[0-9a-f]{8}-?[0-9a-f]{4}-?[0-9a-f]{4}-?[0-9a-f]{4}-?[0-9a-f]{12}$";
        return uuid.matches(regex);
    }
}