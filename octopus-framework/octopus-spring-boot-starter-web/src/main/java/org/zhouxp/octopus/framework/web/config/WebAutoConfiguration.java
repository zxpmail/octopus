package org.zhouxp.octopus.framework.web.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zhouxp.octopus.framework.web.advise.ApiResultWrapperAdvice;
import org.zhouxp.octopus.framework.web.properties.WebProperties;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-07-11 16:34:30
 *
 * @author zhouxp
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({WebProperties.class})
public class WebAutoConfiguration {
    @Bean
    @ConditionalOnProperty(name = "octopus.web.api-result-enable", havingValue = "true",matchIfMissing = true)
    public ApiResultWrapperAdvice apiResultWrapperAdvice(WebProperties webProperties) {
        return new ApiResultWrapperAdvice(webProperties.getIgnorePackageOrClass());
    }
}
