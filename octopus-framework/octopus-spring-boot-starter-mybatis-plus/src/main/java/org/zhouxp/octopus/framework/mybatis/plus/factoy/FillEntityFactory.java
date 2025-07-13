package org.zhouxp.octopus.framework.mybatis.plus.factoy;

import jakarta.servlet.http.HttpServletRequest;
import org.zhouxp.octopus.framework.mybatis.plus.model.FillEntity;
import org.zhouxp.octopus.framework.mybatis.plus.model.FillRule;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-07-13 10:13:19
 *
 * @author zhouxp
 */
public class FillEntityFactory {
    private static final Map<String, Class<?>> TYPE_MAP = new HashMap<>();

    static {
        TYPE_MAP.put("java.lang.String", String.class);
        TYPE_MAP.put("java.util.Date", Date.class);
        TYPE_MAP.put("java.time.LocalDateTime", LocalDateTime.class);
        TYPE_MAP.put("java.lang.Long", Long.class);
        TYPE_MAP.put("java.lang.Integer", Integer.class);
    }

    public static FillEntity create(FillRule rule) {
        Function<HttpServletRequest, Object> supplier = null;

        if ("header".equalsIgnoreCase(rule.getSourceType())) {
            supplier = req -> req.getHeader(rule.getSourceKey());
        } else if ("param".equalsIgnoreCase(rule.getSourceType())) {
            supplier = req -> req.getParameter(rule.getSourceKey());
        } else {
            supplier = req -> getDefaultByRule(rule);
        }

        boolean insertOnly = rule.getInsertOnly() != null && rule.getInsertOnly();
        boolean updateOnly = rule.getUpdateOnly() != null && rule.getUpdateOnly();

        return new FillEntity(
                rule.getFieldName(),
                TYPE_MAP.getOrDefault(rule.getFieldType(), Object.class),
                insertOnly,
                updateOnly,
                supplier
        );
    }

    private static Object getDefaultByRule(FillRule rule) {
        String defaultValue = rule.getDefaultValue();
        if (defaultValue == null || defaultValue.trim().isEmpty()) {
            return getDefaultByType(rule.getFieldType());
        }

        try {
            if ("java.util.Date".equals(rule.getFieldType())) {
                return new Date();
            } else if ("java.time.LocalDateTime".equals(rule.getFieldType())) {
                return LocalDateTime.now();
            } else if ("java.lang.Long".equals(rule.getFieldType())) {
                return Long.valueOf(defaultValue);
            } else if ("java.lang.Integer".equals(rule.getFieldType())) {
                return Integer.valueOf(defaultValue);
            } else if ("java.lang.String".equals(rule.getFieldType())) {
                return defaultValue;
            }
            return null;
        } catch (Exception e) {
            return getDefaultByType(rule.getFieldType());
        }
    }

    private static Object getDefaultByType(String fieldType) {
        return switch (fieldType) {
            case "java.util.Date" -> new Date();
            case "java.time.LocalDateTime" -> LocalDateTime.now();
            case "java.lang.Long" -> 0L;
            case "java.lang.String" -> "system";
            case "java.lang.Integer" -> 0;
            default -> null;
        };
    }
}
