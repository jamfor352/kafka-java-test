package com.example.kafkajavatest.kafka;

import com.example.kafkajavatest.AvroClassTwo;
import com.example.kafkajavatest.AvroClassOne;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Component
public class AllTopicsHandler {
    private final List<AvroClassOne> avroClassOnes = new ArrayList<>();
    private final List<AvroClassTwo> avroClassTwos = new ArrayList<>();
    private final InteractiveQueryService interactiveQueryService;

    public List<AvroClassOne> getAvroClassOnes() {
        return Collections.unmodifiableList(avroClassOnes);
    }
    public List<AvroClassTwo> getAvroClassTwos() {
        return Collections.unmodifiableList(avroClassTwos);
    }


    public void handleAvroClassOne(final AvroClassOne message) {
        avroClassOnes.add(message);
    }


    public void handleAvroClassTwo(final AvroClassTwo message) {
        avroClassTwos.add(message);
    }
}
