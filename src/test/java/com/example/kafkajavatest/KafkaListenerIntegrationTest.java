package com.example.kafkajavatest;

import com.example.kafkajavatest.kafka.AllTopicsHandler;
import com.example.kafkajavatest.kafka.KafkaConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(properties = {
        "spring.cloud.stream.kafka.binder.brokers=${spring.embedded.kafka.brokers}",
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"
})
@DirtiesContext
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1,
        topics = {"my-topic", "my-second-topic", "my-third-topic"})
public class KafkaListenerIntegrationTest {

    @Autowired
    private AllTopicsHandler topicHandler;

    @Autowired
    private KafkaConfig kafkaConfig;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Test
    public void testMyCustomTopicConsumer() {

        final var classOneObject = AvroClassOne.newBuilder()
                .setName("James")
                .setAge(31)
                .build();
        final var classTwoObject = AvroClassTwo.newBuilder()
                        .setWeight("60KG")
                        .setHeight("180CM")
                        .build();
        kafkaConfig.send(classOneObject);
        kafkaConfig.send(classTwoObject);

        await().atMost(Duration.of(10, ChronoUnit.SECONDS)).untilAsserted(
                () ->
                        assertThat(topicHandler.getAvroClassOnes())
                                .isNotEmpty()
        );

        await().atMost(Duration.of(10, ChronoUnit.SECONDS)).untilAsserted(
                () ->
                assertThat(topicHandler.getAvroClassOnes())
                        .containsExactly(classOneObject)
        );

        await().atMost(Duration.of(10, ChronoUnit.SECONDS)).untilAsserted(
                () ->
                        assertThat(topicHandler.getAvroClassTwos())
                                .isNotEmpty()
        );

        await().atMost(Duration.of(10, ChronoUnit.SECONDS)).untilAsserted(
                () ->
                        assertThat(topicHandler.getAvroClassTwos())
                                .containsExactly(classTwoObject)
        );

        assertThat(embeddedKafkaBroker.getTopics()).containsExactly("my-topic");
    }
}
