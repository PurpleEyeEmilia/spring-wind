package com.github.springwind.core.mq.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/9/16 11:24
 * @Desc
 */
@Component
@Slf4j
public class MessageSender {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public Boolean sendDirectMsg(String exchange, String routingKey, String msg) {
        try {
            rabbitTemplate.setExchange(exchange);
            rabbitTemplate.setRoutingKey(routingKey);
            rabbitTemplate.convertAndSend(msg);
            log.info("Send Message To Exchange: {}, RoutingKey: {}, Message Content: {}", exchange, routingKey, msg);
            return true;
        } catch (Exception e) {
            log.error("Send Message Exception: ", e);
            return false;
        }
    }

    public Boolean sendTopicMsg(String msg) {
        return false;
    }

    public Boolean sendFanoutMsg(String exchange, String msg) {
        try {
            rabbitTemplate.setExchange(exchange);
            rabbitTemplate.convertAndSend(msg);
            log.info("Send Message To exchange: {}, Message Content: {}", exchange, msg);
            return true;
        } catch (Exception e) {
            log.error("Send Message Exception: ", e);
            return false;
        }
    }
}
