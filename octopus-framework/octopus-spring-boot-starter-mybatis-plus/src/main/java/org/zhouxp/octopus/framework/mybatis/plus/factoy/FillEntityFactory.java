package org.zhouxp.octopus.framework.mybatis.plus.factoy;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.zhouxp.octopus.framework.common.utils.ConvertUtils;
import org.zhouxp.octopus.framework.mybatis.plus.enums.FillMode;
import org.zhouxp.octopus.framework.mybatis.plus.model.FillEntity;
import org.zhouxp.octopus.framework.mybatis.plus.model.FillRule;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-07-13 10:13:19
 *
 * @author zhouxp
 */
public class FillEntityFactory {

    public static FillEntity create(FillRule rule) {
        String fieldName = rule.getFieldName();
        Class<?> fieldType = ConvertUtils.getClassByName(rule.getFieldType());
        FillMode mode = rule.getMode();

        String sourceKey = rule.getSourceKey();
        String defaultValueStr = rule.getDefaultValue();
        // 默认值兜底
        Object result = ConvertUtils.convert(fieldType, defaultValueStr);

        try {
            var request = getCurrentRequest();
            if (request != null) {
                // 1. 先尝试从 header 获取
                String headerValue = request.getHeader(sourceKey);
                if (headerValue != null && !headerValue.isEmpty()) {
                    Object value = ConvertUtils.convert(fieldType, headerValue);
                    if (value != null) {
                        result = value;
                    }
                    return new FillEntity(fieldName, fieldType, result, mode);
                }

                // 2. header 没有，尝试从 param 获取
                String paramValue = request.getParameter(sourceKey);
                if (paramValue != null && !paramValue.isEmpty()) {
                    Object value = ConvertUtils.convert(fieldType, paramValue);
                    if (value != null) {
                        result = value;
                    }
                }
            }
        } catch (Exception ignore) {
            // 忽略异常，使用默认值
        }

        return new FillEntity(fieldName, fieldType, result, mode);
    }
    private static HttpServletRequest getCurrentRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return attributes != null ? attributes.getRequest() : null;
        } catch (Exception e) {
            return null;
        }
    }
}
