package com.github.springwind.core.canal.service;

import com.alibaba.otter.canal.client.CanalConnector;
import com.github.springwind.core.canal.config.CanalProperties;
import com.github.springwind.core.canal.service.abs.AbstractCanalService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/9/6 14:53
 * @Desc
 */
@Slf4j
public class CanalService extends AbstractCanalService {

    @Resource
    private CanalProperties canalProperties;

    @Resource
    private CanalConnector canalConnector;

    private CanalService client;

    public void startup() {
        //初始化参数
        this.init();

        //启动
        this.start();

        //钩子程序，优雅关机
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                log.info("Canal client stopping");
                client.stop();
            } catch (Exception e) {
                log.error("Something goes wrong when stopping canal", e);
            } finally {
                log.info("Canal client is shutdown");
            }
        }));
    }

    private void init() {
        this.setDestination(canalProperties.getInstance());
        this.setCanalConnector(canalConnector);
        client = this;
    }

}
