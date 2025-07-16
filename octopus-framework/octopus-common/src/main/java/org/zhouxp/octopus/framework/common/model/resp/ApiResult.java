package org.zhouxp.octopus.framework.common.model.resp;

import lombok.Getter;
import lombok.Setter;
import org.zhouxp.octopus.framework.common.model.enums.CommonResponseEnum;
import org.zhouxp.octopus.framework.common.model.interfaces.ICommonResponse;

/**
 * 统一返回格式封装类
 * @author zhouxp
 */
@Getter
@Setter
@SuppressWarnings({"unused", "unchecked"})
public class ApiResult<T> {

    private Integer code;
    private String message;
    private T data;

    private ApiResult() {
        this.code = CommonResponseEnum.SUCCESS.getCode();
        this.message = CommonResponseEnum.SUCCESS.getMessage();
    }

    private ApiResult(T data) {
        this();
        this.data = data;
    }

    private ApiResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private ApiResult(ICommonResponse response) {
        this.code = response.getCode();
        this.message = response.getMessage();
    }

    private ApiResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean isOk(){
       return CommonResponseEnum.SUCCESS.getCode().equals( code);
    }
    // ========== Success Methods ==========

    public static <T> ApiResult<T> ok() {
        return new ApiResult<>();
    }

    public static <T> ApiResult<T> ok(T o) {
        if (o == null) {
            return ok();
        }
        if (o instanceof ApiResult<?>) {
            return (ApiResult<T>) o;
        }
        return new ApiResult<>(o);
    }

    public static <T> ApiResult<T> ok(String message) {
        return new ApiResult<>(CommonResponseEnum.SUCCESS.getCode(), message, null);
    }

    public static <T> ApiResult<T> ok(T data, String message) {
        return new ApiResult<>(CommonResponseEnum.SUCCESS.getCode(), message, data);
    }

    public static <T> ApiResult<T> ok(Integer code, String message) {
        return new ApiResult<>(code, message);
    }

    public static <T> ApiResult<T> ok(Integer code, String message, T data) {
        return new ApiResult<>(code, message, data);
    }

    // ========== Fail Methods ==========

    public static <T> ApiResult<T> fail() {
        return new ApiResult<>(CommonResponseEnum.ERROR);
    }

    public static <T> ApiResult<T> fail(ICommonResponse response) {
        return new ApiResult<>(response);
    }

    public static <T> ApiResult<T> fail(String message) {
        return new ApiResult<>(CommonResponseEnum.ERROR.getCode(), message, null);
    }

    public static <T> ApiResult<T> fail(Integer code, String message) {
        return new ApiResult<>(code, message, null);
    }

    public static <T> ApiResult<T> fail(Integer code, String message, T data) {
        return new ApiResult<>(code, message, data);
    }
}
