package com.bestwaiting.kafka.consumer.consumerBean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;

/**
 * Created by bestwaiting on 17/10/30.
 */
public class Main {
    public static void javaConfig() {
        ApplicationContext context = new AnnotationConfigApplicationContext(KafkaConsumerContext.class);
        PollableChannel fromKafka = context.getBean("received", PollableChannel.class);
        Message<?> received = fromKafka.receive(1000);
        while (true) {
            System.out.println(received);
            received = fromKafka.receive(10000);
        }
    }

    public static void main(String[] args) {
        javaConfig();
    }
}
