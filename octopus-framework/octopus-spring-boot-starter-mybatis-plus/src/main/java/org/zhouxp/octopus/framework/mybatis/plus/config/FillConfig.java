package org.zhouxp.octopus.framework.mybatis.plus.config;

import org.zhouxp.octopus.framework.mybatis.plus.factoy.FillEntityFactory;
import org.zhouxp.octopus.framework.mybatis.plus.model.FillEntity;
import org.zhouxp.octopus.framework.mybatis.plus.model.FillRule;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

    private final List<FillEntity> fillRules;

    public FillConfig(MybatisPlusProperties mybatisPlusProperties) {
        List<FillRule> rules = mybatisPlusProperties.getRules();
        this.fillRules = Optional.ofNullable(rules)
                .filter(r -> !r.isEmpty())
                .map(rs -> rs.stream()
                        .map(FillEntityFactory::create)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    public List<FillEntity> getFillRules() {
        return Collections.unmodifiableList(fillRules);
    }
}
