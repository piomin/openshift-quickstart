package pl.redhat.samples.eventdriven;

import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.streams.kstream.KTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SpringBootApplication
public class ConsumerStreamsApp {

    private static final Logger LOG = LoggerFactory.getLogger(ConsumerStreamsApp.class);

    public static void main(String[] args) {
        SpringApplication.run(ConsumerStreamsApp.class, args);
    }

    @Bean
    public Consumer<KTable<Integer, String>> eventConsumer() {
        return value -> value.toStream().foreach((key,v) -> LOG.info("K:{}, V:{}", key, v));
    }

//    public BiConsumer<KTable<Integer, String>, KTable<Integer, String>> eventConsumer() {
//
//    }
}
