package com.github.springwind.core.canal.runner;

import com.github.springwind.core.canal.service.CanalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/9/6 14:51
 * @Desc canal监听服务启动
 */
@Slf4j
@Component
public class CanalStartupRunner implements CommandLineRunner {

    @Resource
    private CanalService canalService;

    @Override
    public void run(String... args) throws Exception {
        log.info("CanalStartupRunner starting···");
        canalService.startup();
    }
}
