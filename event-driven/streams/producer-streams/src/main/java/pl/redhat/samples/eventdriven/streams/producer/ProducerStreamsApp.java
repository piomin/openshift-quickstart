package pl.redhat.samples.eventdriven.streams.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
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
            new Order(1, "NEW"),
            new Order(2, "NEW"),
            new Order(3, "NEW"),
            new Order(1, "PROCESSING")
    ));

    @Bean
    public Supplier<Message<String>> orderSupplier() {
        return () -> MessageBuilder
                .withPayload(orders.peek().getStatus())
                .setHeader(KafkaHeaders.MESSAGE_KEY, orders.poll().getId())
                .build();
    }
}
