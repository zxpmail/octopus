package org.zhouxp.octopus.framework.mybatis.plus.holder;

import jakarta.servlet.http.HttpServletRequest;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-07-13 19:56:14
 *
 * @author zhouxp
 */
public class RequestHolder {
    private static final ThreadLocal<HttpServletRequest> REQUEST_HOLDER = new ThreadLocal<>();

    public static void setRequest(HttpServletRequest request) {
        REQUEST_HOLDER.set(request);
    }

    public static HttpServletRequest getCurrentRequest() {
        return REQUEST_HOLDER.get();
    }

    public static void remove() {
        REQUEST_HOLDER.remove();
    }
}
