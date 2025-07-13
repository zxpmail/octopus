package org.zhouxp.octopus.framework.mybatis.plus.autofill.factoy;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.zhouxp.octopus.framework.common.utils.ConvertUtils;
import org.zhouxp.octopus.framework.mybatis.plus.autofill.model.FillEntity;
import org.zhouxp.octopus.framework.mybatis.plus.autofill.model.FillRule;
import org.zhouxp.octopus.framework.mybatis.plus.autofill.provider.HeaderSourceProvider;
import org.zhouxp.octopus.framework.mybatis.plus.autofill.provider.ParamSourceProvider;
import org.zhouxp.octopus.framework.mybatis.plus.autofill.provider.SourceProvider;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

/**
 * <p/>
 * {@code @description}  : 填充实体工厂类
 * <p/>
 * <b>@create:</b> 2025-07-13 10:13:19
 *
 * @author zhouxp
 */
public class FillEntityFactory {

    private static final SourceProvider HEADER_PROVIDER = new HeaderSourceProvider();
    private static final SourceProvider PARAM_PROVIDER = new ParamSourceProvider();

    public static List<FillEntity> createAll(List<FillRule> rules) {
        List<FillEntity> result = new ArrayList<>();

        for (FillRule rule : rules) {
            result.add(create(rule));
        }

        return result;
    }

    public static FillEntity create(FillRule rule) {
        String sourceKey = rule.getSourceKey();
        String defaultVal = rule.getDefaultValue();
        Class<?> type = ConvertUtils.getClassByName(rule.getFieldType());

        Object defaultValue = ConvertUtils.convert(type, defaultVal);
        Supplier<Object> valueSupplier = null;

        // 如果是时间类型字段，直接用当前时间作为 supplier
        if (type == Date.class || type == LocalDateTime.class) {
            valueSupplier = () -> {
                if (type == Date.class) {
                    return new Date();
                } else {
                    return LocalDateTime.now();
                }
            };
        } else {
            HttpServletRequest request = getCurrentRequest();
            if (request != null) {
                String val = HEADER_PROVIDER.getValue(request, sourceKey);
                if (val == null || val.isEmpty()) {
                    val = PARAM_PROVIDER.getValue(request, sourceKey);
                }
                if (val != null && !val.isEmpty()) {
                    Object converted = ConvertUtils.convert(type, val);
                    if (converted != null) {
                        defaultValue = converted;
                    }
                }
            }
        }

        return new FillEntity(
                rule.getFieldName(),
                type,
                defaultValue,
                valueSupplier,
                rule.getMode()
        );
    }

    private static HttpServletRequest getCurrentRequest() {
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return attr != null ? attr.getRequest() : null;
        } catch (Exception e) {
            return null;
        }
    }
}
