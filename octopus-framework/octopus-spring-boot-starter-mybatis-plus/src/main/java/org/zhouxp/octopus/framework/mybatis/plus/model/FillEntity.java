package org.zhouxp.octopus.framework.mybatis.plus.model;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.function.Function;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-07-13 10:06:17
 *
 * @author zhouxp
 */
@Data
@AllArgsConstructor
public class FillEntity {
    private String fieldName;
    private Class<?> fieldType;
    private Boolean insertOnly;
    private Boolean updateOnly;
    private Function<HttpServletRequest, Object> valueSupplier;
}
