package org.zhouxp.octopus.framework.mybatis.plus.autofill.factoy;

import org.zhouxp.octopus.framework.common.utils.ConvertUtils;
import org.zhouxp.octopus.framework.mybatis.plus.autofill.model.FillEntity;
import org.zhouxp.octopus.framework.mybatis.plus.autofill.model.FillRule;

import java.util.List;

/**
 * <p/>
 * {@code @description}  : 填充实体工厂类
 * <p/>
 * <b>@create:</b> 2025-07-13 10:13:19
 *
 * @author zhouxp
 */
public class FillEntityFactory {
    public static List<FillEntity> createAll(List<FillRule> rules) {
        if (rules == null) {
            // 返回空列表而不是抛出异常
            return List.of();
        }
        return rules.stream()
                .map(FillEntityFactory::create)
                .toList();
    }

    public static FillEntity create(FillRule rule) {
        return new FillEntity(
                rule.getFieldName(),
                ConvertUtils.getClassByName(rule.getFieldType()),
                rule.getSourceKey(),
                rule.getDefaultValue(),
                rule.getMode()
        );
    }
}
