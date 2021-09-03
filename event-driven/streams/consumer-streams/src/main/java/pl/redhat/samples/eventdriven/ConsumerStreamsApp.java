package pl.redhat.samples.eventdriven;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.redhat.samples.eventdriven.domain.Order;

import java.util.function.Consumer;

@SpringBootApplication
public class ConsumerStreamsApp {

    private static final Logger LOG = LoggerFactory.getLogger(ConsumerStreamsApp.class);

    public static void main(String[] args) {
        SpringApplication.run(ConsumerStreamsApp.class, args);
    }

    @Bean
    public Consumer<KTable<Integer, Order>> eventConsumer() {
        return value -> value.toStream().foreach((key,v) -> LOG.info("Table: key={}, val={}", key, v));
    }

    @Bean
    public Consumer<KStream<Integer, Order>> eventStream() {
        return input -> input.foreach((key, value) -> LOG.info("Stream: key={}, val={}", key, value));
    }

//    @Bean
//    public BiFunction<KTable<Integer, String>, KTable<Integer, String>, KTable<Integer, CustomerOrder>> process() {
//        return (tableOrders, tableCustomers) -> (tableOrders.leftJoin(tableCustomers, (orderId, order) -> order.toUpperCase()));
//    }

}
