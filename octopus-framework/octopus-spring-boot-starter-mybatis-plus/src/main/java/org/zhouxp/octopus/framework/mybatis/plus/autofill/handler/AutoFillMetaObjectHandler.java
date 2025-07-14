package org.zhouxp.octopus.framework.mybatis.plus.autofill.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Primary;
import org.zhouxp.octopus.framework.common.utils.ConvertUtils;
import org.zhouxp.octopus.framework.mybatis.plus.autofill.factoy.FillEntityFactory;
import org.zhouxp.octopus.framework.mybatis.plus.autofill.model.FillEntity;
import org.zhouxp.octopus.framework.mybatis.plus.autofill.model.FillRule;
import org.zhouxp.octopus.framework.mybatis.plus.autofill.provider.HeaderSourceProvider;
import org.zhouxp.octopus.framework.mybatis.plus.autofill.provider.ParamSourceProvider;
import org.zhouxp.octopus.framework.mybatis.plus.autofill.provider.SourceProvider;
import org.zhouxp.octopus.framework.mybatis.plus.holder.RequestHolder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * <p/>
 * {@code @description}  : 自定义字段自动填充处理类
 * <p/>
 * <b>@create:</b> 2025-07-11 19:17:30
 *
 * @author zhouxp
 */
@Primary
public class AutoFillMetaObjectHandler implements MetaObjectHandler {
    private final List<FillEntity> rules;
    private static final SourceProvider HEADER_PROVIDER = new HeaderSourceProvider();
    private static final SourceProvider PARAM_PROVIDER = new ParamSourceProvider();

    public AutoFillMetaObjectHandler(List<FillRule> fillRules) {
        this.rules = FillEntityFactory.createAll(fillRules);
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        handleFill(metaObject, true);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        handleFill(metaObject, false);
    }

    private void handleFill(MetaObject metaObject, boolean isInsert) {
        for (FillEntity rule : rules) {
            if (rule.shouldFill(isInsert)) {
                setFieldValue(metaObject, rule);
            }
        }
    }

    private void setFieldValue(MetaObject metaObject, FillEntity rule) {
        HttpServletRequest request = RequestHolder.getCurrentRequest();
        Object value = resolveValue(request, rule);

        if (value != null && metaObject.hasSetter(rule.fieldName())) {
            metaObject.setValue(rule.fieldName(), value);
        }
    }

    private Object resolveValue(HttpServletRequest request, FillEntity rule) {
        Class<?> type = rule.fieldType();
        String sourceKey = rule.sourceKey();
        String defaultValue = rule.defaultValue();
        if (type == Date.class || type == LocalDateTime.class) {
            return type == Date.class ? new Date() : LocalDateTime.now();
        }
        if (request != null) {
            String val = HEADER_PROVIDER.getValue(request, sourceKey);
            if (val == null || val.isEmpty()) {
                val = PARAM_PROVIDER.getValue(request, sourceKey);
            }
            if (val != null && !val.isEmpty()) {
                defaultValue = val;
            }
        }

        return ConvertUtils.convert(type, defaultValue);
    }
}
