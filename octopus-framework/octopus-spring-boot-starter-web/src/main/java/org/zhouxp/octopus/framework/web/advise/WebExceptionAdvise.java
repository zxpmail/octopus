package org.zhouxp.octopus.framework.web.advise;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.zhouxp.octopus.framework.common.constant.CommonConstants;
import org.zhouxp.octopus.framework.common.exception.BaseException;
import org.zhouxp.octopus.framework.common.model.enums.CommonResponseEnum;
import org.zhouxp.octopus.framework.common.model.interfaces.ICommonResponse;
import org.zhouxp.octopus.framework.common.model.resp.ApiResult;
import org.zhouxp.octopus.framework.common.utils.ExceptionUtil;
import org.zhouxp.octopus.framework.web.annotations.NoApiResult;
import org.zhouxp.octopus.framework.web.enums.WebResponseEnum;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * <p/>
 * {@code @description}  : 统一异常处理类
 * <p/>
 * <b>@create:</b> 2025-07-09 11:13:26
 *
 * @author zhouxp
 */
@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class WebExceptionAdvise {

    @Value("${spring.application.name:test}")
    private  String module;

    /**
     * NoHandlerFoundException 404 异常处理
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ApiResult<ICommonResponse> handlerNoHandlerFoundException(NoHandlerFoundException e) throws Throwable {
        errorDispose(e);
        outPutErrorWarn(NoHandlerFoundException.class, WebResponseEnum.NOT_FOUND, e);
        return ApiResult.fail(WebResponseEnum.NOT_FOUND);
    }


    /**
     * 处理BaseException子类异常
     *
     * @param e BaseException及子类
     * @return ApiResult包装异常
     */
    @ExceptionHandler(value = BaseException.class)
    public Object handleBusinessException(BaseException e) throws Throwable {
        errorDispose(e);
        log.error(ExceptionUtil.getMessage(e));
        if (e.getICommonResponse() == null) {
            return ApiResult.fail(e.getMessage());
        }
        log.error(CommonConstants.MESSAGE, module, e.getICommonResponse().getMessage());
        return ApiResult.fail(e.getICommonResponse());
    }

    /**
     * Exception 类捕获 500 异常处理
     */
    @ExceptionHandler(value = Exception.class)
    public ApiResult<ICommonResponse> handlerException(Exception e) throws Throwable {
        errorDispose(e);
        return ifDepthExceptionType(e);
    }

    /**
     * 二次深度检查错误类型
     */
    private ApiResult<ICommonResponse> ifDepthExceptionType(Throwable throwable) {
        Throwable cause = throwable.getCause();
        outPutError(Exception.class, CommonResponseEnum.ERROR, throwable);
        String message = (cause != null ? cause.getMessage() : throwable.getMessage());
        return ApiResult.fail(message);
    }




    /**
     * HttpRequestMethodNotSupportedException 405 异常处理
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResult<ICommonResponse> handlerHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) throws Throwable {
        errorDispose(e);
        outPutErrorWarn(HttpRequestMethodNotSupportedException.class,
                WebResponseEnum.NOT_ALLOWED, e);
        return ApiResult.fail(WebResponseEnum.NOT_ALLOWED);
    }

    /**
     * HttpMediaTypeNotSupportedException 415 异常处理
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ApiResult<ICommonResponse> handlerHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException e) throws Throwable {
        errorDispose(e);
        outPutErrorWarn(HttpMediaTypeNotSupportedException.class,
                WebResponseEnum.UNSUPPORTED_MEDIA_TYPE, e);
        return ApiResult.fail(WebResponseEnum.UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * HttpMessageNotReadableException 参数错误异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResult<ICommonResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) throws Throwable {
        errorDispose(e);
        Throwable rootCause = e.getRootCause();
        String msg;
        if (rootCause != null) {
            msg = String.format("%s : 错误详情( %s )", WebResponseEnum.PARAM_ERROR.getMessage(), rootCause.getMessage());
        } else {
            msg = WebResponseEnum.PARAM_ERROR.getMessage();
        }
        outPutError(HttpMessageNotReadableException.class, WebResponseEnum.PARAM_ERROR, e);
        return ApiResult.fail(WebResponseEnum.PARAM_ERROR.getCode(), msg);
    }


    /**
     * ConstraintViolationException 参数错误异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResult<ICommonResponse> handleConstraintViolationException(ConstraintViolationException e) throws Throwable {
        errorDispose(e);
        String smg = "";
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        if (log.isDebugEnabled()) {
            for (ConstraintViolation<?> error : constraintViolations) {
                log.error("{} -> {}", error.getPropertyPath(), error.getMessageTemplate());
                smg = error.getMessageTemplate();
            }
        }

        if (constraintViolations.isEmpty()) {
            return ApiResult.fail(WebResponseEnum.BUSINESS_ERROR);
        }

        return ApiResult.fail(WebResponseEnum.Validation_ERROR.getCode(), smg);
    }

    /**
     * MethodArgumentNotValidException 参数错误异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult<ICommonResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) throws Throwable {
        errorDispose(e);
        BindingResult bindingResult = e.getBindingResult();
        return getBindResult(bindingResult);
    }

    /**
     * BindException 参数错误异常
     */
    @ExceptionHandler(BindException.class)
    public ApiResult<ICommonResponse> handleBindException(BindException e) throws Throwable {
        errorDispose(e);
        outPutError(BindException.class, WebResponseEnum.PARAM_ERROR, e);
        BindingResult bindingResult = e.getBindingResult();
        return getBindResult(bindingResult);
    }

    private ApiResult<ICommonResponse> getBindResult(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (log.isDebugEnabled()) {
            for (FieldError error : fieldErrors) {
                log.error("{} -> {}", error.getDefaultMessage(), error.getDefaultMessage());
            }
        }

        if (fieldErrors.isEmpty()) {
            return ApiResult.fail(WebResponseEnum.BUSINESS_ERROR);
        }

        return ApiResult.fail(WebResponseEnum.PARAM_ERROR.getCode(), fieldErrors.getFirst().getDefaultMessage());
    }

    /**
     * 处理Controller层相关异常
     */
    @ExceptionHandler({
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            TypeMismatchException.class,
            HttpMessageNotWritableException.class,
            HttpMediaTypeNotAcceptableException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class
    })
    public ApiResult<ICommonResponse> handleServletException(Exception e) {
        log.error(CommonConstants.MESSAGE, module, ExceptionUtil.getMessage(e));
        return ApiResult.fail(e.getMessage());
    }


    /**
     * 校验是否进行异常处理
     *
     * @param e   异常
     * @param <T> extends Throwable
     * @throws Throwable 异常
     */
    private <T extends Throwable> void errorDispose(T e) throws Throwable {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return;
        }
        HttpServletRequest request = requestAttributes.getRequest();
        HandlerMethod handlerMethod = (HandlerMethod) request.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingHandler");
        if (handlerMethod == null) {
            return;
        }
        // 获取异常 Controller
        Class<?> beanType = handlerMethod.getBeanType();
        // 获取异常方法
        Method method = handlerMethod.getMethod();

        NoApiResult classAnnotation = beanType.getAnnotation(NoApiResult.class);
        NoApiResult methodAnnotation = method.getAnnotation(NoApiResult.class);

        if (classAnnotation != null && !classAnnotation.value()) {
            throw e;
        }
        if (methodAnnotation != null && !methodAnnotation.value()) {
            throw e;
        }
    }

    public void outPutError(Class<?> errorType, Enum<?> secondaryErrorType, Throwable throwable) {
        log.error("[{}: {}] {}: {}", module, errorType.getSimpleName(), secondaryErrorType, throwable.getMessage(),
                throwable);
    }

    public void outPutErrorWarn(Class<?> errorType, Enum<?> secondaryErrorType, Throwable throwable) {
        log.warn("[{}: {}] {}: {}", module, errorType.getSimpleName(), secondaryErrorType, throwable.getMessage());
    }
}
