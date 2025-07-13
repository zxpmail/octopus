package org.zhouxp.octopus.framework.mybatis.plus.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.zhouxp.octopus.framework.mybatis.plus.model.FillRule;

import java.util.List;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-07-13 09:52:49
 *
 * @author zhouxp
 */
@ConfigurationProperties(prefix = "octopus.mybatis-plus")
public class MybatisPlusProperties {
    private List<FillRule> rules;

    public List<FillRule> getRules() {
        return rules;
    }

    public void setRules(List<FillRule> rules) {
        this.rules = rules;
    }
}
