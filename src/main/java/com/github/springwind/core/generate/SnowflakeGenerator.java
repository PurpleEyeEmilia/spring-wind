package com.github.springwind.core.generate;

import com.github.springwind.common.exception.CommonException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/9/16 14:39
 * @Desc 雪花id生产器
 */
@Component
public class SnowflakeGenerator {

    /**
     * 0 00000000000000000000000000000000000000000 00000 00000 000000000000
     */
    private static final Long TOP = 0L;

    /**
     * 起始时间戳 2019-10-10 14:16:10
     */
    private static final Long START_TIMESTAMP = 1570688170L;

    /**
     * 序列占用位数
     */
    private static final Long SEQUENCE_BIT = 12L;

    /**
     * 机器中心占用位数
     */
    private static final Long WORKER_BIT = 5L;

    /**
     * 数据中心占用位数
     */
    private static final Long DATA_CENTER_BIT = 5L;

    /**
     * 时间戳占用位数
     */
    private static final Long TIMESTAMP_BIT = 41L;

    /**
     * 机器中心左移位数
     */
    private static final Long WORKER_LEFT = SEQUENCE_BIT;

    /**
     * 数据中心左移位数
     */
    private static final Long DATA_CENTER_LEFT = WORKER_LEFT + WORKER_BIT;

    /**
     * 时间戳左移位数
     */
    private static final Long TIMESTAMP_LEFT = WORKER_LEFT + WORKER_BIT + TIMESTAMP_BIT;

    /**
     * 序列号最大值
     */
    private static final Long SEQUENCE_MAX = ~(1L << SEQUENCE_BIT);

    /**
     * 机器中心最大值
     */
    private static final Long WORKER_MAX = ~(1L << WORKER_BIT);

    /**
     * 数据中心最大值
     */
    private static final Long DATA_CENTER_MAX = ~(1L << DATA_CENTER_BIT);

    /**
     * 上一次发号的时间戳
     */
    private static long lastTimestamp = -1L;

    /**
     * 上一次发号的序列号
     */
    private static long sequence = 0L;

    /**
     * 工作机器id
     */
    @Value("${snowflake.workId}")
    private long workId;

    /**
     * 数据中心id
     */
    @Value("${snowflake.dataCenterId}")
    private long dataCenterId;

    public synchronized Long getNextId() {
        long currentTimeMillis = System.currentTimeMillis();

        if (currentTimeMillis < START_TIMESTAMP) {
            throw new CommonException("currentTimeMillis < startTimestamp, refuse generate Snowflake id");
        }

        if (currentTimeMillis < lastTimestamp) {
            throw new CommonException("clock roll back，refuse generate Snowflake id");
        }

        if (currentTimeMillis == lastTimestamp) {
            sequence = (sequence + 1) & SEQUENCE_MAX;
            if (sequence == 0L) {
                currentTimeMillis = waitNextTimeMillis();
            }
        }

        lastTimestamp = currentTimeMillis;

        return (currentTimeMillis - START_TIMESTAMP) << TIMESTAMP_LEFT | dataCenterId << DATA_CENTER_LEFT | workId << WORKER_LEFT | sequence;
    }

    private static long waitNextTimeMillis() {
        long currentTimeMillis = System.currentTimeMillis();
        while (currentTimeMillis <= lastTimestamp) {
            currentTimeMillis = System.currentTimeMillis();
        }
        return currentTimeMillis;
    }
}
