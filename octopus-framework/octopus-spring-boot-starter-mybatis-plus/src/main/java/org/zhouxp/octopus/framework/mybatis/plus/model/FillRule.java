package org.zhouxp.octopus.framework.mybatis.plus.model;

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
public class FillRule {
    /**
     * 填充字段名
     */
    private String fieldName;
    /**
     * 填充字段类型 类型字符串，如 java.lang.String
     */
    private String fieldType;
    /**
     * 插入时填充
     */
    private Boolean insertOnly;
    /**
     * 更新时填充
     */
    private Boolean updateOnly;
    /**
     * 填充数据源类型 header / param / default
     */
    private String sourceType;
    /**
     * 填充数据源字段名  如 userID
     */
    private String sourceKey;
    /**
     * 默认值
     */
    private String defaultValue;
}
