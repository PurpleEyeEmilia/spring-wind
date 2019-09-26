package com.github.springwind.core.canal.entity;

import lombok.Data;

import java.util.List;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/9/12 15:03
 * @Desc
 */
@Data
public class CanalMsg {
    /**
     * 数据库名称
     */
    private String dbName;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 事件类型（insert，update，delete）
     */
    private String eventType;

    /**
     * 变更前数据
     */
    private List<CanalChangeInfo> beforeData;

    /**
     * 变更后数据
     */
    private List<CanalChangeInfo> afterData;

}
