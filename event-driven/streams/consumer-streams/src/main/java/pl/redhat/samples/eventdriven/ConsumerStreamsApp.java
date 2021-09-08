package pl.redhat.samples.eventdriven;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.redhat.samples.eventdriven.domain.Customer;
import pl.redhat.samples.eventdriven.domain.CustomerOrder;
import pl.redhat.samples.eventdriven.domain.Order;

import java.time.Duration;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@SpringBootApplication
public class ConsumerStreamsApp {

    private static final Logger LOG = LoggerFactory.getLogger(ConsumerStreamsApp.class);

    public static void main(String[] args) {
        SpringApplication.run(ConsumerStreamsApp.class, args);
    }

    @Bean
    public Consumer<KTable<Integer, Order>> eventConsumer() {
        return input -> input.toStream().foreach((key, value) -> LOG.info("Table: key={}, val={}", key, value));
    }

    @Bean
    public Consumer<KStream<Integer, Order>> eventStream() {
        return input -> input.groupByKey()
                .windowedBy(TimeWindows.of(Duration.ofMillis(10000)))
                .count()
                .toStream()
                .foreach((key, value) -> LOG.info("Stream: key={}, val={}", key, value));
//        return input -> input.foreach((key, value) -> LOG.info("Stream: key={}, val={}", key, value));
    }

    @Bean
    public BiFunction<KTable<Integer, Order>, KTable<Integer, Customer>, KTable<Integer, CustomerOrder>> process() {
        return (tableOrders, tableCustomers) -> tableOrders
                .leftJoin(tableCustomers, Order::getCustomerId,
                        (order, customer) -> new CustomerOrder(order.getId(), customer.getId(), customer.getName(), order.getStatus()));
    }

}
