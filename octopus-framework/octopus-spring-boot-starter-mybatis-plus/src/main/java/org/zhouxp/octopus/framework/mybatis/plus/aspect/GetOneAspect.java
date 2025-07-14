package org.zhouxp.octopus.framework.mybatis.plus.aspect;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Arrays;

/**
 * <p/>
 * {@code @description}  : GetOne Aop
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
        Object[] originalArgs = joinPoint.getArgs();
        try {
            if (originalArgs == null || originalArgs.length == 0) {
                QueryWrapper<?> wrapper = new QueryWrapper<>();
                wrapper.last(limitSql);
                return joinPoint.proceed(new Object[]{wrapper});
            }

            if (!(originalArgs[0] instanceof Wrapper<?> wrapper)) {
                QueryWrapper<?> wrapper = new QueryWrapper<>();
                wrapper.last(limitSql);
                originalArgs[0]=wrapper;
                return joinPoint.proceed(originalArgs);
            }
            if (wrapper.getTargetSql().toLowerCase().contains(limitSql.toLowerCase())) {
                return joinPoint.proceed();
            }
            if (wrapper instanceof QueryWrapper) {
                ((QueryWrapper<?>) wrapper).last(limitSql);
            } else if (wrapper instanceof LambdaQueryWrapper) {
                ((LambdaQueryWrapper<?>) wrapper).last(limitSql);
            } else {
                wrapper = new LambdaQueryWrapper<>();
                ((LambdaQueryWrapper<?>) wrapper).last(limitSql);
            }
            Object[] newArgs = Arrays.copyOf(originalArgs, originalArgs.length);
            newArgs[0] = wrapper;
            return joinPoint.proceed(newArgs);
        } catch (Exception e) {
            if (limitSql != null) {
                log.error("Error adding limitSql: {}", limitSql, e);
            } else {
                log.error("Error with null limitSql", e);
            }
            throw new RuntimeException("Failed to process selectOne with limitSql", e);
        }
    }
}
