package org.zhouxp.octopus.framework.mybatis.plus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Primary;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.zhouxp.octopus.framework.mybatis.plus.config.FillConfig;
import org.zhouxp.octopus.framework.mybatis.plus.model.FillEntity;

/**
 * <p/>
 * {@code @description}  : 自定义字段自动填充处理类
 * <p/>
 * <b>@create:</b> 2025-07-11 19:17:30
 *
 * @author zhouxp
 */
@Slf4j
@RequiredArgsConstructor
@Primary
public class AutoFillMetaObjectHandler implements MetaObjectHandler {
    private final FillConfig fillConfig;
    @Override
    public void insertFill(MetaObject metaObject) {
        handleFill(metaObject, true);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        handleFill(metaObject, false);
    }

    private void handleFill(MetaObject metaObject, boolean isInsert) {
        for (var rule : fillConfig.getFillRules()) {
            if (rule.shouldFill(isInsert)) {
                setFieldValue(metaObject, rule);
            }
        }
    }

    private void setFieldValue(MetaObject metaObject, FillEntity rule) {
        String fieldName = rule.getFieldName();
        Object value = null;

        try {
            var attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attr != null) {
                HttpServletRequest request = attr.getRequest();
                if (rule.getValueSupplier() != null) {
                    value = rule.getValueSupplier().apply(request);
                }
            }
        } catch (Exception ignore) {}

        // fallback 到默认值
        if (value == null) {
            value = getDefaultByFieldType(rule.getFieldType());
        }

        // 类型转换
        if (value instanceof String strValue) {
            value = convertToTargetType(strValue, rule.getFieldType());
        }

        if (value != null) {
            metaObject.setValue(fieldName, value);
        }
    }

    private Object getDefaultByFieldType(Class<?> fieldType) {
        if (fieldType == java.util.Date.class) {
            return new java.util.Date();
        } else if (fieldType == java.time.LocalDateTime.class) {
            return java.time.LocalDateTime.now();
        } else if (fieldType == Long.class || fieldType == long.class) {
            return System.currentTimeMillis() / 1000L;
        } else if (fieldType == String.class) {
            return "system";
        } else if (fieldType == Integer.class || fieldType == int.class) {
            return 0;
        } else {
            return null;
        }
    }

    private Object convertToTargetType(String value, Class<?> targetType) {
        try {
            if (targetType == Long.class || targetType == long.class) {
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
            } else if (targetType == java.util.Date.class) {
                return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value);
            } else if (targetType == java.time.LocalDateTime.class) {
                return java.time.LocalDateTime.parse(value);
            } else if (targetType == String.class) {
                return value;
            }
        } catch (Exception e) {
            // 转换失败，返回 null 或抛出异常
            // 可记录日志：log.warn("类型转换失败: {} -> {}", value, targetType, e);
        }

        // 默认返回 null，使用 fallback 默认值
        return null;
    }
}
