package org.zhouxp.octopus.framework.web.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-07-11 16:57:01
 *
 * @author zhouxp
 */
@Getter
@Setter
@ConfigurationProperties(prefix = WebProperties.PREFIX)
public class WebProperties {
    public static final String PREFIX = "octopus.web";
    /**
     * 忽略进行统一返回包或类
     */
    private List<String> ignorePackageOrClass = new ArrayList<>() {{
        add("org.springframework");
        add("org.springdoc");
        add("springfox.documentation");
    }};
}
