package org.zhouxp.octopus.framework.mybatis.plus.autofill.model;

import lombok.Data;
import org.zhouxp.octopus.framework.mybatis.plus.autofill.enums.FillMode;

import java.util.function.Supplier;

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
    private final String fieldName;
    private final Class<?> fieldType;
    // 可能是静态值
    private final Object defaultValue;
    // 可能是动态值（如 new Date()）
    private final Supplier<Object> valueSupplier;
    private final FillMode mode;

    public FillEntity(String fieldName, Class<?> fieldType, Object defaultValue, Supplier<Object> valueSupplier, FillMode mode) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.defaultValue = defaultValue;
        this.valueSupplier = valueSupplier != null ? valueSupplier : () -> defaultValue;
        this.mode = mode != null ? mode : FillMode.BOTH;
    }

    public Object getValue() {
        return valueSupplier.get();
    }
    public boolean shouldFill(boolean isInsert) {
        return isInsert ? mode.shouldInsert() : mode.shouldUpdate();
    }
}

