package org.zhouxp.octopus.framework.mybatis.plus.config;

import org.zhouxp.octopus.framework.mybatis.plus.autofill.factoy.FillEntityFactory;
import org.zhouxp.octopus.framework.mybatis.plus.autofill.model.FillEntity;
import org.zhouxp.octopus.framework.mybatis.plus.autofill.model.FillRule;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-07-13 10:38:39
 *
 * @author zhouxp
 */
public class FillConfig {

    private List<FillEntity> fillRules = Collections.emptyList();

    public FillConfig(MybatisPlusProperties mybatisPlusProperties) {
        List<FillRule> rules =mybatisPlusProperties.getRules();
        if (rules != null && !rules.isEmpty()) {
            this.fillRules = rules.stream()
                    .map(FillEntityFactory::create)
                    .collect(Collectors.toList());
        }
    }

    public List<FillEntity> getFillRules() {
        return Collections.unmodifiableList(fillRules);
    }
}
