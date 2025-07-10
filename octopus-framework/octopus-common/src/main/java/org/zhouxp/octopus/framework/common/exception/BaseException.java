package org.zhouxp.octopus.framework.common.exception;

import lombok.Getter;
import org.zhouxp.octopus.framework.common.model.enums.CommonResponseEnum;
import org.zhouxp.octopus.framework.common.model.interfaces.ICommonResponse;

/**
 * <p/>
 * {@code @description}  : 基础异常处理类
 * <p/>
 * <b>@create:</b> 2025-07-07 16:50:14
 *
 * @author zhouxp
 */
@Getter
@SuppressWarnings("unused")
public class BaseException extends RuntimeException{
    /**
     * 对基础响应接口进行处理
     */
    private final ICommonResponse iCommonResponse;


    public BaseException(Throwable cause, ICommonResponse iBaseResponse) {
        super(iBaseResponse.getMessage(),cause);
        this.iCommonResponse = iBaseResponse;
    }

    /**
     * 输入iBaseResponse接口子类进行处理
     */
    public BaseException(ICommonResponse iBaseResponse) {
        super(iBaseResponse.getMessage());
        this.iCommonResponse = iBaseResponse;
    }

    /**
     * 输入字符串消息构造函数进行处理
     * @param message 消息字符串
     */
    public BaseException(String message) {
        super(message);
        this.iCommonResponse = new ICommonResponse() {
            @Override
            public Integer getCode() {
                return CommonResponseEnum.ERROR.getCode();
            }

            @Override
            public String getMessage() {
                return message;
            }
        };
    }

    public static void errorResponse(ICommonResponse iBaseResponse)  {
        throw  exception(iBaseResponse);

    }
    public static RuntimeException exception(ICommonResponse iBaseResponse) {
        if (iBaseResponse == null) {
            return new IllegalArgumentException("iBaseResponse cannot be null");
        }
        return new BaseException( iBaseResponse);
    }
}
