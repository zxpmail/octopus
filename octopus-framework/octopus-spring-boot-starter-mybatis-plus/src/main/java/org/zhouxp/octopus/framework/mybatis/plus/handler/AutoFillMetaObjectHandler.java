package org.zhouxp.octopus.framework.mybatis.plus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Primary;

/**
 * <p/>
 * {@code @description}  : 自定义字段自动填充处理类
 * <p/>
 * <b>@create:</b> 2025-07-11 19:17:30
 *
 * @author zhouxp
 */
@Slf4j
@RequiredArgsConstructor
@Primary
public class AutoFillMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {

    }

    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
