package org.zhouxp.octopus.framework.mybatis.plus.enums;

import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private static final Map<Integer, FillMode> VALUE_MAP = Stream.of(values())
            .collect(Collectors.toMap(
                    FillMode::getValue,
                    mode -> mode,
                    (existing, replacement) -> existing
            ));

    FillMode(int value) {
        this.value = value;
    }

    /**
     * 根据数值获取枚举实例
     */
    public static FillMode fromValue(int value) {
        FillMode mode = VALUE_MAP.get(value);
        return mode != null ? mode : BOTH;
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