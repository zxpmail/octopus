package org.zhouxp.octopus.framework.mybatis.plus.autofill.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Primary;
import org.zhouxp.octopus.framework.mybatis.plus.autofill.factoy.FillEntityFactory;
import org.zhouxp.octopus.framework.mybatis.plus.autofill.model.FillEntity;
import org.zhouxp.octopus.framework.mybatis.plus.autofill.model.FillRule;

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
        String fieldName = rule.getFieldName();
        // 获取动态值
        Object value = rule.getValue();
        if (value != null && metaObject.hasSetter(fieldName)) {
            metaObject.setValue(fieldName, value);
        }
    }
}
