package org.zhouxp.octopus.framework.web.annotations;

import java.lang.annotation.*;

/**
 * <p/>
 * {@code @description}  : 加上此注解不包装返回值
 * <p/>
 * <b>@create:</b> 2025-07-10 18:38:05
 *
 * @author zhouxp
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.PARAMETER,ElementType.TYPE})
@Documented
public @interface NoApiResult {
    /**
     * 是否进行全局异常处理封装
     * @return true:进行处理;  false:不进行异常处理
     */
    boolean value() default true;
}

