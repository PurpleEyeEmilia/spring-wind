package com.github.springwind.core.sync;

import com.github.springwind.core.canal.entity.CanalMsg;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/10/9 10:53
 * @Desc
 */
public interface SyncHandler {
    /**
     * 判断是不是自己关注的消息
     *
     * @param canalMsg
     * @return
     */
    Boolean watch(CanalMsg canalMsg);

    /**
     * 处理消息 需要保证幂等性
     *
     * @param canalMsg
     */
    void handle(CanalMsg canalMsg);

}
