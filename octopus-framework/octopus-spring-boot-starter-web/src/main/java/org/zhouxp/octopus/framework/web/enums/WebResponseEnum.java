package org.zhouxp.octopus.framework.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.zhouxp.octopus.framework.common.model.interfaces.ICommonResponse;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-07-10 17:19:18
 *
 * @author zhouxp
 */
@AllArgsConstructor
@Getter
public enum WebResponseEnum implements ICommonResponse {
    BUSINESS_ERROR(501, "业务异常"),
    NOT_FOUND(404, String.format("哎呀，无法找到这个资源啦(%s)", HttpStatus.NOT_FOUND.getReasonPhrase())),
    NOT_ALLOWED(405, "请求方式不支持"),
    UNSUPPORTED_MEDIA_TYPE(415, "媒体类型不支持"),
    PARAM_ERROR(416, "参数错误"),
    Validation_ERROR(417, "参数验证失败"),
    ;
    private final int code;
    private final String message;
}
