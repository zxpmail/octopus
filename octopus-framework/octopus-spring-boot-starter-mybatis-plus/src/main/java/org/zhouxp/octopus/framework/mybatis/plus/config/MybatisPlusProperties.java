package org.zhouxp.octopus.framework.mybatis.plus.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.zhouxp.octopus.framework.mybatis.plus.autofill.model.FillRule;

import java.util.List;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-07-13 09:52:49
 *
 * @author zhouxp
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "octopus.mybatis-plus")
public class MybatisPlusProperties {
    private List<FillRule> rules;

}
