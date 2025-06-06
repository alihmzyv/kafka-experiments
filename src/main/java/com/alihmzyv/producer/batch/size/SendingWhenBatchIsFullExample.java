package com.alihmzyv.producer.batch.size;

import java.time.LocalDateTime;
import java.util.Properties;
import java.util.stream.IntStream;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class SendingWhenBatchIsFullExample {
    public static void main(String[] args) throws InterruptedException {
        Properties kafkaProps = new Properties();
        kafkaProps.put("bootstrap.servers", "localhost:9092");
        kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("batch.size", 1000);
        kafkaProps.put("linger.ms", 30_000);
        try (KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(kafkaProps)) {
            IntStream.rangeClosed(1, 100)
                    .forEach(num -> {
                        ProducerRecord<String, String> record = new ProducerRecord<>("test", "data" + num);
                        System.out.println("Sending the record " + num);
                        if (num == 68) {
                            System.out.println("Second batch created at " + LocalDateTime.now());
                        }
                        kafkaProducer.send(record, ((metadata, exception) -> {
                            if (exception != null) {
                                System.out.printf("Failed to send the record %s%n%s", num,
                                        ExceptionUtils.getStackTrace(exception));
                            } else {
                                System.out.println("Sent the record " + num);
                            }
                        }));
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
            Thread.sleep(50_000);
        }
    }
}
