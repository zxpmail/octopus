package org.zhouxp.octopus.framework.mybatis.plus.autofill.model;

import org.zhouxp.octopus.framework.mybatis.plus.autofill.enums.FillMode;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-07-13 10:06:17
 *
 * @author zhouxp
 */
public record FillEntity(String fieldName, Class<?> fieldType, String sourceKey, String defaultValue, FillMode mode) {

    public boolean shouldFill(boolean isInsert) {
        return isInsert ? mode.shouldInsert() : mode.shouldUpdate();
    }
}

