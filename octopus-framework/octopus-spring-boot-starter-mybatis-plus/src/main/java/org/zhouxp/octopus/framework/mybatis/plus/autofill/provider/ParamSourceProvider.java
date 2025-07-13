package org.zhouxp.octopus.framework.mybatis.plus.autofill.provider;

import jakarta.servlet.http.HttpServletRequest;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-07-13 17:24:23
 *
 * @author zhouxp
 */
public class ParamSourceProvider implements SourceProvider {
    @Override
    public String getValue(HttpServletRequest request, String key) {
        return request.getParameter(key);
    }
}
