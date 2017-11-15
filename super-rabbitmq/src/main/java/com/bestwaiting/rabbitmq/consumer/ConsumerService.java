package com.bestwaiting.rabbitmq.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

/**
 * Created by bestwaiting on 17/11/14.
 */
@Service
public class ConsumerService implements MessageListener {
    @Override
    public void onMessage(Message message) {
        System.out.println("消息消费者 = " + message.toString());
    }
}
