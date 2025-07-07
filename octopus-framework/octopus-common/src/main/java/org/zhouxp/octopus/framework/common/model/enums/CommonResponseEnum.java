package org.zhouxp.octopus.framework.common.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zhouxp.octopus.framework.common.model.interfaces.ICommonResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-07-07 16:56:49
 *
 * @author zhouxp
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("unused")
public enum CommonResponseEnum implements ICommonResponse {
    /**
     * 操作成功时默认消息
     */
    SUCCESS(200,"操作成功！"),
    /**
     * 操作失败时默认消息
     */
    ERROR(500, "操作失败"),

    ;

    private static final Map<Integer, String> MAP;
    static {
        MAP = Arrays.stream(CommonResponseEnum.values()).collect(HashMap::new, (m, v) -> m.put(v.code, v.message), HashMap::putAll);
    }
    /**
     * 响应状态码
     */
    private  Integer code;

    /**
     * 响应信息
     */
    private  String message;

    /**
     * 通过code获取message
     */
    public static String getMessage(Integer code) {
        return code == null ? null : MAP.get(code);
    }
}
