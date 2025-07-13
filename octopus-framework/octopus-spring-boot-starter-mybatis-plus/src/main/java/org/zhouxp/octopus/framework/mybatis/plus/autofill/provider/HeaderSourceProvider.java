package org.zhouxp.octopus.framework.mybatis.plus.autofill.provider;

import jakarta.servlet.http.HttpServletRequest;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-07-13 17:25:05
 *
 * @author zhouxp
 */
public class HeaderSourceProvider implements SourceProvider {
    @Override
    public String getValue(HttpServletRequest request, String key) {
        return request.getHeader(key);
    }
}
