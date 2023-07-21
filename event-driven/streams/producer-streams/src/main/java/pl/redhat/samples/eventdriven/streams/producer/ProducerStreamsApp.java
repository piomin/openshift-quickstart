package pl.redhat.samples.eventdriven.streams.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import pl.redhat.samples.eventdriven.streams.producer.message.Customer;
import pl.redhat.samples.eventdriven.streams.producer.message.Order;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

@SpringBootApplication
public class ProducerStreamsApp {

    public static void main(String[] args) {
        SpringApplication.run(ProducerStreamsApp.class, args);
    }

    LinkedList<Order> orders = new LinkedList<>(List.of(
            new Order(1, "NEW", 3),
            new Order(2, "NEW", 1),
            new Order(3, "NEW", 2),
            new Order(1, "PROCESSING", 3),
            new Order(2, "FINISHED", 1)
    ));

    @Bean
    public Supplier<Message<Order>> orderSupplier() {
        return () -> orders.peek() != null ? MessageBuilder
                .withPayload(orders.peek())
                .setHeader(KafkaHeaders.KEY, orders.poll().getId())
                .build() : null;
    }

    LinkedList<Customer> customers = new LinkedList<>(List.of(
            new Customer(1, "AAAAA"),
            new Customer(2, "BBBBB"),
            new Customer(3, "CCCCC")
    ));

    @Bean
    public Supplier<Message<Customer>> customerSupplier() {
        return () -> customers.peek() != null ? MessageBuilder
                .withPayload(customers.peek())
                .setHeader(KafkaHeaders.KEY, customers.poll().getId())
                .build() : null;
    }
}
