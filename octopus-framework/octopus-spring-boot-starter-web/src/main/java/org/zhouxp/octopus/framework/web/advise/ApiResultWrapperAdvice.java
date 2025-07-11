package org.zhouxp.octopus.framework.web.advise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.zhouxp.octopus.framework.common.model.resp.ApiResult;
import org.zhouxp.octopus.framework.web.annotations.NoApiResult;

import java.util.List;

/**
 * <p/>
 * {@code @description}  : 统一返回值处理
 * <p/>
 * <b>@create:</b> 2025-07-11 15:44:34
 *
 * @author zhouxp
 */
@ControllerAdvice
public class ApiResultWrapperAdvice implements ResponseBodyAdvice<Object> {

    private static final Logger logger = LoggerFactory.getLogger(ApiResultWrapperAdvice.class);

    private final List<String> ignorePackageOrClass;

    public ApiResultWrapperAdvice(List<String> ignorePackageOrClass) {
        this.ignorePackageOrClass = ignorePackageOrClass;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !(returnType.hasMethodAnnotation(NoApiResult.class)
                || ApiResult.class.isAssignableFrom(returnType.getParameterType()))
                || !ignorePackageOrClass.contains(returnType.getDeclaringClass().getName());
    }

    @Override
    public Object beforeBodyWrite(@Nullable Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        try {
            if (body == null) {
                logger.warn("Empty response body detected for request: {} {}", request.getMethod(), request.getURI());
                return ApiResult.ok();
            }

            if (body instanceof String) {
                // 不要 toString，交给 Spring 序列化
                return ApiResult.ok(body);
            }

            return ApiResult.ok(body);

        } catch (Exception e) {
            logger.error("Error wrapping response body for request: {} {}: {}",
                    request.getMethod(), request.getURI(), e.getMessage(), e);
            // 推荐：抛出异常由全局异常处理器处理
            throw new RuntimeException("Failed to wrap response", e);
        }
    }
}