package org.zhouxp.octopus.framework.mybatis.plus.autofill.enums;

import lombok.Getter;

/**
 * <p/>
 * {@code @description}  : 填充模式
 * <p/>
 * <b>@create:</b> 2025-07-13 15:37:49
 *
 * @author zhouxp
 */
@Getter
public enum FillMode {
    INSERT(1),
    UPDATE(2),
    BOTH(3);

    private final int value;

    FillMode(int value) {
        this.value = value;
    }

    /**
     * 是否应该在插入时填充
     */
    public boolean shouldInsert() {
        return this == INSERT || this == BOTH;
    }

    /**
     * 是否应该在更新时填充
     */
    public boolean shouldUpdate() {
        return this == UPDATE || this == BOTH;
    }
}