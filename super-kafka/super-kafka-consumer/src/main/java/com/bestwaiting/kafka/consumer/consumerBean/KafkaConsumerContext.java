package com.bestwaiting.kafka.consumer.consumerBean;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.messaging.PollableChannel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bestwaiting on 17/10/30.
 */
@Configuration
public class KafkaConsumerContext {

    private String brokerAddress="127.0.0.1:8977";

    @Bean
    public KafkaMessageListenerContainer container() throws Exception {
        Map<String, Object> props = new HashMap();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "testGroup");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.brokerAddress);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 100);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15000);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new KafkaMessageListenerContainer(new DefaultKafkaConsumerFactory(props),
                new ContainerProperties("test"));
    }

    @Bean
    public KafkaMessageDrivenChannelAdapter<String, String> adapter(KafkaMessageListenerContainer<String, String> container) {
        KafkaMessageDrivenChannelAdapter<String, String> kafkaMessageDrivenChannelAdapter =
                new KafkaMessageDrivenChannelAdapter(container);
        kafkaMessageDrivenChannelAdapter.setOutputChannel(received());
        kafkaMessageDrivenChannelAdapter.setAutoStartup(true);
        return kafkaMessageDrivenChannelAdapter;
    }

    @Bean("received")
    public PollableChannel received() {
        return new QueueChannel();
    }
}
