package org.zhouxp.octopus.framework.common.model.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p/>
 * {@code @description}  : 查询数据表分页时返回实体类
 * <p/>
 * <b>@create:</b> 2025-07-14 09:40:04
 *
 * @author zhouxp
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuppressWarnings("unused")
public class PageResult implements Serializable {
    /**
     * 返回总条数
     */
    private Long total;
    /**
     * 返回记录数
     */
    private Object list;
}