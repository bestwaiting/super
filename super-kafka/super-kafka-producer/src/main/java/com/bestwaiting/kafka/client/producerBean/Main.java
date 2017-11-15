package com.bestwaiting.kafka.client.producerBean;

import com.bestwaiting.kafka.client.producerBean.KafkaClientContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;

/**
 * Created by bestwaiting on 17/10/30.
 */
public class Main {
    public static void javaConfig() {
        ApplicationContext context = new AnnotationConfigApplicationContext(KafkaClientContext.class);
        MessageChannel inputToKafka = context.getBean("inputToKafka", MessageChannel.class);
        for (int i = 0; i < 5; i++) {
            Message<String> message = new GenericMessage<String>("test_" + i);
            boolean flag = inputToKafka.send(message);
            System.out.println(flag + "_" + i);
        }
    }

    public static void main(String[] args) {
        javaConfig();
    }
}
