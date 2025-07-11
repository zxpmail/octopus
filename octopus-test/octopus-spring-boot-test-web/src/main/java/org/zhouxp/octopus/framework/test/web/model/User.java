package org.zhouxp.octopus.framework.test.web.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

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
public class User {
    @Max(4)
    @Min(1)
    private Integer age;
    @Length(min = 2, max = 40)
    private String name;
    @Email
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime createTime;

    private Boolean deleted;
}
