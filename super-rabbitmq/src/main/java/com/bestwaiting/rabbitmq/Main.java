package com.bestwaiting.rabbitmq;

import com.bestwaiting.rabbitmq.provider.ProviderService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by bestwaiting on 17/11/14.
 */
public class Main {
    private ApplicationContext context = null;

    @Before
    public void setUp() throws Exception {
        context = new ClassPathXmlApplicationContext("spring-mq.xml");
    }

    private String queueId = "test_mq";

    @Test
    public void should_send_a_amq_message() throws Exception {

        ProviderService producer = (ProviderService) context.getBean("providerService");
        producer.sendQueue(queueId + "_exchange", queueId + "_patt", "Hello, I am amq sender");
    }
}
