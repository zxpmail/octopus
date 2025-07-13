package org.zhouxp.octopus.framework.mybatis.plus.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zhouxp.octopus.framework.mybatis.plus.autofill.handler.AutoFillMetaObjectHandler;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-07-13 17:07:08
 *
 * @author zhouxp
 */
@Configuration
@EnableConfigurationProperties(MybatisPlusProperties.class)
public class AutoMybatisPlusAutoConfiguration {
    @ConditionalOnProperty(prefix = "octopus.mybatis-plus", name = "enabled", havingValue = "true", matchIfMissing = true)
    @Bean
    public AutoFillMetaObjectHandler autoFillMetaObjectHandler(FillConfig fillConfig) {
        return new AutoFillMetaObjectHandler(fillConfig);
    }

    @ConditionalOnProperty(prefix = "octopus.mybatis-plus", name = "enabled", havingValue = "true", matchIfMissing = true)
    @Bean
    public FillConfig fillConfig(MybatisPlusProperties properties) {
        return new FillConfig(properties);
    }
}
