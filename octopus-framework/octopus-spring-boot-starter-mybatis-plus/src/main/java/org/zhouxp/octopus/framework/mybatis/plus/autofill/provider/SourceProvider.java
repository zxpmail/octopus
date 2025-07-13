package org.zhouxp.octopus.framework.mybatis.plus.autofill.provider;

import jakarta.servlet.http.HttpServletRequest;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-07-13 17:23:40
 *
 * @author zhouxp
 */
public interface SourceProvider {
    String getValue(HttpServletRequest request, String key);
}
