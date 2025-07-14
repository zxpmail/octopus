package org.zhouxp.octopus.framework.mybatis.plus.aspect;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-07-14 16:39:55
 *
 * @author zhouxp
 */
@Aspect
@Slf4j
public class GetOneAspect {
    private final String limitSql;

    public GetOneAspect(String limitSql) {
        this.limitSql = limitSql;
    }

    @Around("execution(* com.baomidou.mybatisplus.core.mapper.*.selectOne(..))")
    public Object addGetOneParam(ProceedingJoinPoint joinPoint) throws Throwable {
        Wrapper<?> wrapper;
        Object[] originalArgs = joinPoint.getArgs();
        try {
            if (originalArgs == null || originalArgs.length == 0) {
                log.error("Arguments are null or empty");
                return joinPoint.proceed(originalArgs);
            }

            if (!(originalArgs[0] instanceof Wrapper)) {
                log.error("First argument is not a Wrapper");
                return joinPoint.proceed(originalArgs);
            }

            wrapper = (Wrapper<?>) originalArgs[0];
            if (wrapper instanceof QueryWrapper) {
                ((QueryWrapper<?>) wrapper).last(limitSql);
            } else if (wrapper instanceof LambdaQueryWrapper) {
                ((LambdaQueryWrapper<?>) wrapper).last(limitSql);
            } else {
                wrapper = new LambdaQueryWrapper<>();
                ((LambdaQueryWrapper<?>) wrapper).last(limitSql);
            }
            originalArgs[0] = wrapper;
        } catch (Exception e) {
            if (limitSql != null) {
                log.error("params error:{}", limitSql, e);
            } else {
                log.error("params error with null limitSql", e);
            }
        }
        return joinPoint.proceed(originalArgs);
    }
}
