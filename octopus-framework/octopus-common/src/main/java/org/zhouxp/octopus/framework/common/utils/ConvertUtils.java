package org.zhouxp.octopus.framework.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-07-13 16:25:45
 *
 * @author zhouxp
 */
public class ConvertUtils {
    /**
     * 根据类名字符串获取 Class 对象
     */
    public static Class<?> getClassByName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("无法找到类: " + className, e);
        }
    }

    /**
     * 将字符串值转换为目标类型
     */
    public static Object convert(Class<?> targetType, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            if (targetType == String.class) {
                return value;

            } else if (targetType == Long.class || targetType == long.class) {
                return Long.valueOf(value);

            } else if (targetType == Integer.class || targetType == int.class) {
                return Integer.valueOf(value);

            } else if (targetType == Short.class || targetType == short.class) {
                return Short.valueOf(value);

            } else if (targetType == Byte.class || targetType == byte.class) {
                return Byte.valueOf(value);

            } else if (targetType == Boolean.class || targetType == boolean.class) {
                return Boolean.valueOf(value);

            } else if (targetType == Double.class || targetType == double.class) {
                return Double.valueOf(value);

            } else if (targetType == Float.class || targetType == float.class) {
                return Float.valueOf(value);

            } else if (targetType == Date.class) {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value);

            } else if (targetType == java.time.LocalDateTime.class) {
                return java.time.LocalDateTime.parse(value);

            } else {
                // 默认返回字符串
                return value;
            }
        } catch (Exception e) {
            // 转换失败返回 null
            return null;
        }
    }
}
