package com.alihmzyv.consumer.max.poll.interval.ms;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class MaxPollIntervalMsExample {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test-consumers");
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("heartbeat.interval.ms", 10_000);
        props.put("session.timeout.ms", 30_000);
        props.put("auto.commit.interval.ms", 15_000);
        props.put("max.poll.records", 5);
        props.put("max.poll.interval.ms", 15_000);
        props.put("auto.offset.reset", "earliest");

        try (KafkaConsumer<String, String> consumer =
                     new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singleton("test"));
            Duration timeout = Duration.ofMillis(100);
            int countOfRecords = 0;
            while (true) {
                System.out.println("Polling at " + LocalDateTime.now());
                ConsumerRecords<String, String> records = consumer.poll(timeout);
                System.out.println("Poll() returned records: " + records.count());
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("Consumed offset = %d, key = %s, value = %s from topic = %s, partition = %d\n",
                            record.offset(), record.key(), record.value(), record.topic(), record.partition());
                    countOfRecords++;
                }
                if (countOfRecords > 10) {
                    System.out.println("Count of records " + countOfRecords + " is more than 10, sleeping...");
                    Thread.sleep(16_000);
                } else {
                    System.out.println("Sleeping after 5 records..");
                    Thread.sleep(5_000);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
