package com.github.springwind.core.canal.entity;

import lombok.Data;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/9/12 15:17
 * @Desc
 */
@Data
public class CanalChangeInfo {
    /**
     * 字段名
     */
    private String columnName;

    /**
     * 字段值
     */
    private String columnValue;

    /**
     * 该字段是否有更新
     */
    private Boolean update;
}
