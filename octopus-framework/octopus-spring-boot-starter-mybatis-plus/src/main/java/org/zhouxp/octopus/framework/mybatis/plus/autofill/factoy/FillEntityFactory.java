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

import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * {@code @description}  :
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

        Object value = ConvertUtils.convert(type, defaultVal);

        HttpServletRequest request = getCurrentRequest();
        if (request != null) {
            String val = HEADER_PROVIDER.getValue(request, sourceKey);
            if (val == null || val.isEmpty()) {
                val = PARAM_PROVIDER.getValue(request, sourceKey);
            }
            if (val != null && !val.isEmpty()) {
                Object converted = ConvertUtils.convert(type, val);
                if (converted != null) {
                    value = converted;
                }
            }
        }

        return new FillEntity(
                rule.getFieldName(),
                type,
                value,
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
