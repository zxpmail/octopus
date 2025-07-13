package org.zhouxp.octopus.framework.mybatis.plus.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.zhouxp.octopus.framework.mybatis.plus.enums.FillMode;

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
     * 填充模式  INSERT, UPDATE, BOTH
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private FillMode mode;;
    /**
     * 填充数据源字段名  如 userID
     */
    private String sourceKey;
    /**
     * 默认值
     */
    private String defaultValue;
}
