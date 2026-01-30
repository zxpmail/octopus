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

import java.util.List;

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
    private static final int DEGRADE_PAGE_NUM = 1;
    private static final int DEGRADE_PAGE_SIZE = 1;
    private static final boolean DEGRADE_SEARCH_COUNT = false;
    @Around("execution(* com.baomidou.mybatisplus.core.mapper.*.selectOne(..))")
    public Object handleSelectOneSafely(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (TooManyResultsException e) {
            return degradeToFirstRecord(joinPoint);
        }
    }
    @SuppressWarnings({"rawtypes", "unchecked"})
    private Object degradeToFirstRecord(ProceedingJoinPoint joinPoint)  {
        Object target = joinPoint.getTarget();
        if (!(target instanceof BaseMapper mapper)) {
            throw new IllegalArgumentException("Target is not an instance of BaseMapper");
        }

        Object[] args = joinPoint.getArgs();
        Wrapper wrapper = null;
        if (args != null && args.length > 0 && args[0] instanceof Wrapper w) {
            wrapper = w;
        }

        log.warn("Degraded selectOne due to too many results. Mapper: {}, Wrapper: {}",
                mapper.getClass().getSimpleName(), wrapper);

        IPage page = mapper.selectPage(new Page(DEGRADE_PAGE_NUM, DEGRADE_PAGE_SIZE, DEGRADE_SEARCH_COUNT), wrapper);

        List<Object> records = page.getRecords();
        if (records == null || records.isEmpty()) {
            return null;
        }

        return records.stream().findFirst().orElse(null);
    }
}
