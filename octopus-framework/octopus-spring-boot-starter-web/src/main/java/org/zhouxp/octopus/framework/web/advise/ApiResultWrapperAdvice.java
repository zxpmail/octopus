package org.zhouxp.octopus.framework.web.advise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.zhouxp.octopus.framework.common.model.resp.ApiResult;
import org.zhouxp.octopus.framework.web.annotations.NoApiResult;

import java.util.Arrays;
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

    private final List<String> ignorePatterns = Arrays.asList(
            "com.github.xiaoymin.knife4j.",
            "org.springframework.boot.actuate.",
            "org.springdoc."
    );

    private boolean shouldIgnore(Class<?> declaringClass) {
        String className = declaringClass.getName();
        for (String pattern : ignorePatterns) {
            if (className.startsWith(pattern)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {

        // 1. 显式排除
        if (returnType.hasMethodAnnotation(NoApiResult.class)) {
            return false;
        }

        // 2. 已是 ApiResult
        if (ApiResult.class.isAssignableFrom(returnType.getParameterType())) {
            return false;
        }

        // 3. 忽略第三方包
        if (shouldIgnore(returnType.getDeclaringClass())) {
            return false;
        }

        // 4. 二进制/ResponseEntity 排除
        Class<?> rt = returnType.getParameterType();
        if (rt == byte[].class || ResponseEntity.class.isAssignableFrom(rt)) {
            return false;
        }

        // 5. ByteArray converter 排除
        return !ByteArrayHttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @Override
    public Object beforeBodyWrite( @Nullable Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        if (body == null) {
            logger.warn("Empty response body for: {} {}", request.getMethod(), request.getURI());
            return ApiResult.ok();
        }
        return ApiResult.ok(body);
    }
}