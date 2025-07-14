package org.zhouxp.octopus.framework.mybatis.plus.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.zhouxp.octopus.framework.mybatis.plus.autofill.handler.AutoFillMetaObjectHandler;
import org.zhouxp.octopus.framework.mybatis.plus.interceptor.RequestInterceptor;

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
public class AutoMybatisPlusAutoConfiguration  implements WebMvcConfigurer {
    @ConditionalOnProperty(prefix = "octopus.mybatis-plus", name = "auto-fill", havingValue = "true", matchIfMissing = true)
    @Bean
    public AutoFillMetaObjectHandler autoFillMetaObjectHandler(MybatisPlusProperties properties) {
        return new AutoFillMetaObjectHandler(properties.getRules());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestInterceptor());
    }
}
