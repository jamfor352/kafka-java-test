package com.example.kafkajavatest.kafka;

import com.example.kafkajavatest.AvroClassOne;
import com.example.kafkajavatest.AvroClassTwo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class KafkaConfig {

    private final AllTopicsHandler topicHandler;

    private final BlockingQueue<SpecificRecordBase> queue = new LinkedBlockingQueue<>();
    public void send(SpecificRecordBase someAvroRecord) {
        queue.offer(someAvroRecord);
    }

    @Bean
    public Supplier<SpecificRecordBase> myTopicProducer() {
        return () -> queue.poll();
    }
    @Bean
    public Function<AvroClassOne, AvroClassOne> AvroClassOne() {
        return input -> {
            log.info("AvroClassOne received AvroClassOne: {}", input);
            return input;
        };
    }

    @Bean
    public Function<AvroClassTwo, AvroClassTwo> AvroClassTwo() {
        return input -> {
            log.info("AvroClassTwo received AvroClassTwo: {}", input);
            return input;
        };
    }

    @Bean
    public Consumer<AvroClassOne> storeAvroClassOne() {
        return input -> {
            log.info("storeAvroClassOne received AvroClassOne: {}", input);
            topicHandler.handleAvroClassOne(input);
        };
    }

    @Bean
    public Consumer<AvroClassTwo> storeAvroClassTwo() {
        return input -> {
            log.info("storeAvroClassTwo received AvroClassTwo: {}", input);
            topicHandler.handleAvroClassTwo(input);
        };
    }

}