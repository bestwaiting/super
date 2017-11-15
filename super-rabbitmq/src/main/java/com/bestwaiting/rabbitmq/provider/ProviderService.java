package com.bestwaiting.rabbitmq.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by bestwaiting on 17/11/14.
 */
@Service
public class ProviderService {

    Logger logger = LoggerFactory.getLogger(ProviderService.class);
    @Resource
    private AmqpTemplate amqpTemplate;

    public void sendQueue(String exchange_key, String queue_key, Object object) {
        // convertAndSend 将Java对象转换为消息发送至匹配key的交换机中Exchange
        amqpTemplate.convertAndSend(exchange_key, queue_key, object);
        logger.warn(object.toString());
    }
}
