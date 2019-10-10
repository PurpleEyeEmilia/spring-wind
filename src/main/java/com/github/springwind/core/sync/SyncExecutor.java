package com.github.springwind.core.sync;

import com.github.springwind.core.canal.entity.CanalMsg;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/10/9 10:53
 * @Desc
 */
@Component
public class SyncExecutor {

    private List<SyncHandler> handlerList = new ArrayList<>();

    /**
     * 执行同步
     *
     * @param canalMsg
     */
    public void process(CanalMsg canalMsg) {
        if (!CollectionUtils.isEmpty(handlerList)) {
            handlerList.forEach(syncHandler -> syncHandler.handle(canalMsg));
        }
    }

    /**
     * 注册
     *
     * @param syncHandler
     */
    public void register(SyncHandler syncHandler) {
        if (!handlerList.contains(syncHandler)) {
            handlerList.add(syncHandler);
        }
    }
}
