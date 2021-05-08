package com.github.springwind.core.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.github.springwind.common.annotation.DistributedLock;
import com.github.springwind.core.canal.entity.CanalMsg;
import com.github.springwind.core.mq.constants.AmqpContants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/9/16 14:39
 * @Desc 同步mysql数据到es，消费端
 */
@Component
@Slf4j
@EnableScheduling
public class EsSyncConsumer {

    private static final String LOCK_KEY = "canal:es:lock:key";

    @RabbitListener(
            bindings = @QueueBinding(
                    exchange = @Exchange(value = AmqpContants.CANAL_FANOUT_EXCHANGE, ignoreDeclarationExceptions = "true"),
                    value = @Queue(value = AmqpContants.CANAL_TO_ES_QUEUE, declare = "true")
            )
    )
    @Scheduled(fixedRate = 30 * 1000)
    @DistributedLock(lockKey = LOCK_KEY)
    public void handleMessage(String msg) {
        log.info("EsSyncConsumer Receive Message: {}", msg);
        CanalMsg canalMsg = JSON.parseObject(msg, CanalMsg.class);

    }

}
