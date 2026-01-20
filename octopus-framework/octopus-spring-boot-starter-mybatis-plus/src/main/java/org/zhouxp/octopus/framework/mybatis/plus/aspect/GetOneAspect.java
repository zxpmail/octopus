package org.zhouxp.octopus.framework.mybatis.plus.aspect;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

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

    @Around("execution(* com.baomidou.mybatisplus.core.mapper.*.selectOne(..))")
    public Object handleSelectOneSafely(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (TooManyResultsException e) {
            return degradeToFirstRecord(joinPoint, e);
        }
    }
    @SuppressWarnings({"rawtypes", "unchecked"})
    private Object degradeToFirstRecord(ProceedingJoinPoint joinPoint, TooManyResultsException originalEx) throws Throwable {
        BaseMapper mapper = (BaseMapper) joinPoint.getTarget();
        Object[] args = joinPoint.getArgs();
        Wrapper wrapper = (args != null && args.length > 0 && args[0] instanceof Wrapper)
                ? (Wrapper) args[0]
                : null;

        log.warn("Degraded selectOne due to too many results. Mapper: {}", mapper.getClass().getSimpleName());

        IPage page = mapper.selectPage(new Page(1, 1, false), wrapper); // raw type 调用
        return page.getRecords().stream().findFirst().orElse(null);
    }
}
