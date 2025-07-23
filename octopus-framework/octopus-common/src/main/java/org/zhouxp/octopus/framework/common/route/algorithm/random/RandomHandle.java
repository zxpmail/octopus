package org.zhouxp.octopus.framework.common.route.algorithm.random;

import org.zhouxp.octopus.framework.common.exception.BaseException;
import org.zhouxp.octopus.framework.common.model.enums.CommonResponseEnum;
import org.zhouxp.octopus.framework.common.route.RouteHandle;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p/>
 * {@code @description}  : 随机路由处理器，从服务列表中随机选择一个服务
 * <p/>
 * <b>@create:</b> 2025-07-23 16:19:29
 *
 * @author zhouxp
 */
public class RandomHandle implements RouteHandle {
    @Override
    public String routeServer(List<String> values, String key) {
        if (values == null || values.isEmpty()) {
            throw new BaseException(CommonResponseEnum.SERVER_NOT_AVAILABLE);
        }
        
        int size = values.size();
        int i = ThreadLocalRandom.current().nextInt(size);
        return values.get(i);
    }
}