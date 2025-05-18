package com.alihmzyv.consumer.heartbeat.interval.ms;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class TwoPartitionsProducer {
    static KafkaProducer<String, String> producer;
    static Admin admin;

    static {
        Properties kafkaProps = new Properties();
        kafkaProps.put("bootstrap.servers", "localhost:9092");
        kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        admin = Admin.create(kafkaProps);
        producer = new KafkaProducer<>(kafkaProps);
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int partitions = 2;
        short replicationFactor = 1;
        createTopic("test", partitions, replicationFactor);
        sendMessages(0, 200, "test");
    }

    private static void createTopic(String topicName, int partitions, short replicationFactor)
            throws ExecutionException, InterruptedException {
        NewTopic testTopic = new NewTopic(topicName, partitions, replicationFactor);
        admin.createTopics(Collections.singleton(testTopic)).all().get();
    }

    private static void sendMessages(int from, int to, String topic) {
        IntStream.rangeClosed(from, to)
                .forEach(num -> {
                    int partition = num % 2;
                    send(topic, num, partition);
                });
    }

    private static void send(String topic, int num, int partition) {
        ProducerRecord<String, String> record =
                new ProducerRecord<>(topic, partition, null, java.lang.String.format("message %s", num));
        System.out.println("Sending the record " + num);
        producer.send(record, ((metadata, exception) -> {
            if (exception != null) {
                System.out.printf("Failed to send the record %s%n%s", num,
                        ExceptionUtils.getStackTrace(exception));
            } else {
                System.out.println("Sent the record " + num);
            }
        }));
    }
}