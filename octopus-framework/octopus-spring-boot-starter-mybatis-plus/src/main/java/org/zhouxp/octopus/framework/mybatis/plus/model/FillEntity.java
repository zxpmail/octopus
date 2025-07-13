package org.zhouxp.octopus.framework.mybatis.plus.model;

import lombok.Data;
import org.zhouxp.octopus.framework.mybatis.plus.enums.FillMode;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-07-13 10:06:17
 *
 * @author zhouxp
 */
@Data
public class FillEntity {
    private String fieldName;
    private Class<?> fieldType;
    private FillMode mode;;
    private final Object defaultValue;

    public FillEntity(String fieldName, Class<?> fieldType, Object defaultValue, FillMode mode) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.defaultValue = defaultValue;
        this.mode = mode != null ? mode : FillMode.BOTH;
    }

    public boolean shouldFill(boolean isInsert) {
        return isInsert ? mode.shouldInsert() : mode.shouldUpdate();
    }

}
