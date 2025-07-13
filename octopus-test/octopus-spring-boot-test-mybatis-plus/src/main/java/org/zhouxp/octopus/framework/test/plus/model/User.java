package org.zhouxp.octopus.framework.test.plus.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-07-11 17:44:36
 *
 * @author zhouxp
 */
@Data
@Accessors(chain = true)
@TableName("user")
public class User {
    private Long id;
    private String username;
    // 自动填充
    @TableField(fill=FieldFill.INSERT)
    private String creator;
    // 自动填充
    @TableField(fill=FieldFill.INSERT)
    private Integer status;
    // 自动填充
    @TableField(fill=FieldFill.INSERT)
    private LocalDateTime createTime;
    // 自动填充
    @TableField(fill=FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
