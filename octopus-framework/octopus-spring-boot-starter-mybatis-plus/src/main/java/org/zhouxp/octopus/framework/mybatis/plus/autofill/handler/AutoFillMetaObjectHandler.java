package org.zhouxp.octopus.framework.mybatis.plus.autofill.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Primary;
import org.zhouxp.octopus.framework.mybatis.plus.config.FillConfig;
import org.zhouxp.octopus.framework.mybatis.plus.autofill.model.FillEntity;

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
        Object value = rule.getDefaultValue();

        if (value != null && metaObject.hasSetter(fieldName)) {
            metaObject.setValue(fieldName, value);
        }
    }
}
