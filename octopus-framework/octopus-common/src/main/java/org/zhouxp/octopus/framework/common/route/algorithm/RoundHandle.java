package org.zhouxp.octopus.framework.common.route.algorithm;

import org.zhouxp.octopus.framework.common.exception.BaseException;
import org.zhouxp.octopus.framework.common.model.enums.CommonResponseEnum;
import org.zhouxp.octopus.framework.common.route.RouteHandle;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p/>
 * {@code @description}  :轮询算法
 * <p/>
 * <b>@create:</b> 2025-07-23 16:54:48
 *
 * @author zhouxp
 */
public class RoundHandle implements RouteHandle {
    private final AtomicInteger index = new AtomicInteger();

    @Override
    public String routeServer(List<String> servers) {
        if (servers == null || servers.isEmpty()) {
            throw new BaseException(CommonResponseEnum.SERVER_NOT_AVAILABLE);
        }
        int size = servers.size();
        int currentIndex = index.incrementAndGet() % size;
        return servers.get(currentIndex);
    }
}
