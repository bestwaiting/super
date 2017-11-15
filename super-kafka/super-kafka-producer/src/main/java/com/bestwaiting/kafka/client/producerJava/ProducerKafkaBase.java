package com.bestwaiting.kafka.client.producerJava;

import com.bestwaiting.kafka.client.common.KafkaConstants;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;

/**
 * Created by bestwaiting on 17/10/30.
 */
public class ProducerKafkaBase {

    private final Producer<String, String> producer;
    private final String brokerList = "127.0.0.1:8977";

    public ProducerKafkaBase() {
        Properties props = new Properties();
        //此处配置的是kafka的端口
        props.put("metadata.broker.list", brokerList);

        //配置value的序列化类
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        //配置key的序列化类
        props.put("key.serializer.class", "kafka.serializer.StringEncoder");

        //0, which means that the producer never waits for an acknowledgement from the broker.
        //1, which means that the producer gets an acknowledgement after the leader replica has received the data.
        //-1, which means that the producer gets an acknowledgement after all in-sync replicas have received the data.
        props.put("request.required.acks", "-1");
        producer = new Producer<String, String>(new ProducerConfig(props));
    }

    /**
     * 生产者生成消息
     *
     * @param topic
     * @param key
     * @param message
     */
    void produceMessage(String topic, String key, String message) {
        producer.send(new KeyedMessage<String, String>(topic, key, message));
    }

    public static void main(String[] args) {
        ProducerKafkaBase kafkaBase = new ProducerKafkaBase();
        for (int i = 0; i < 1000; i++) {
            String key = String.valueOf(i);
            String message = "the kafka message is " + key;
            kafkaBase.produceMessage(KafkaConstants.TOPIC, key, message);
            System.out.println(message);
        }
    }
}
