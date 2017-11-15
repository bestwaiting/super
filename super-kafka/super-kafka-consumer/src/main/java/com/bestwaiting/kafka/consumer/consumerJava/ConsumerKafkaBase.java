package com.bestwaiting.kafka.consumer.consumerJava;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by bestwaiting on 17/10/30.
 */
public class ConsumerKafkaBase {
    private final ConsumerConnector consumer;
    private final String zookeeper = "127.0.0.1:8976";

    public ConsumerKafkaBase() {

        Properties props = new Properties();
        //zookeeper 配置
        props.put("zookeeper.connect", zookeeper);
        //group 代表一个消费组
        props.put("group.id", "jd-group");
        //zk连接超时
        props.put("zookeeper.session.timeout.ms", "4000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "smallest");
        //序列化类
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        ConsumerConfig config = new ConsumerConfig(props);

        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(config);
    }

    void consumerMessage(String topic, int num) {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, num);

        StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
        StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());

        Map<String, List<KafkaStream<String, String>>> consumerMap =
                consumer.createMessageStreams(topicCountMap, keyDecoder, valueDecoder);
        for (KafkaStream<String, String> stream : consumerMap.get(topic)) {
            ConsumerIterator<String, String> it = stream.iterator();
            while (it.hasNext())
                System.out.println(it.next().message());
        }
    }

    public static void main(String[] args) {

        new ConsumerKafkaBase().consumerMessage("test", 1);

    }
}
