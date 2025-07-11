package org.zhouxp.octopus.framework.mybatis.plus.handler.model;

import lombok.Data;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-07-11 19:26:23
 *
 * @author zhouxp
 */
@Data
public class FillEntity {
    private String fieldName;
    private String fieldType;
    private Boolean insertOnly;
    private Boolean updateOnly;
}
