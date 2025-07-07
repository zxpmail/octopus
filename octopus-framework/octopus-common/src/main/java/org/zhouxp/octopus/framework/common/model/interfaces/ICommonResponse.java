package org.zhouxp.octopus.framework.common.model.interfaces;

/**
 * <p/>
 * {@code @description}  : 异常响应接口
 * <p/>
 * <b>@create:</b> 2025-07-07 16:51:18
 *
 * @author zhouxp
 */
public interface ICommonResponse {
    /**
     * 取得响应代码
     * @return 响应代码值
     */
    Integer getCode();

    /**
     * 取得响应消息
     * @return 响应消息
     */
    String getMessage();
}
