package pl.redhat.samples.eventdriven.order;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.redhat.samples.eventdriven.order.domain.Order;
import pl.redhat.samples.eventdriven.order.message.ConfirmCommand;
import pl.redhat.samples.eventdriven.order.message.OrderCommand;
import pl.redhat.samples.eventdriven.order.message.OrderQuery;
import pl.redhat.samples.eventdriven.order.message.OrderQueryResult;
import pl.redhat.samples.eventdriven.order.repository.OrderRepository;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@SpringBootApplication
public class OrderQueryServiceApp {

    private static final Logger LOG = LoggerFactory.getLogger(OrderQueryServiceApp.class);

    public static void main(String[] args) {
        SpringApplication.run(OrderQueryServiceApp.class, args);
    }

    @Autowired
    OrderRepository orderRepository;

    @Bean
    public Consumer<OrderCommand> orders() {
        return input -> orderRepository
                .save(new Order(
                        input.getId(),
                        input.getCustomerId(),
                        input.getProductId(),
                        input.getProductCount(),
                        input.getAmount(),
                        "RESERVATION"));
    }

//    @Bean
//    public BiConsumer<KStream<Integer, OrderCommand>, KStream<Integer, ConfirmCommand>> orders() {
//        return (input1, input2) -> input1.map((k, v) -> new KeyValue<>(v.getCustomerId(),
//                        new Order(v.getId(), v.getCustomerId(), v.getProductId(), v.getProductCount(), v.getAmount(), "RESERVATION")))
//                .merge(input2.map((k2, v2) -> new KeyValue<>(v2.getCustomerId(),
//                        new Order(null, v2.getCustomerId(), v2.getProductId(), v2.getProductCount(), v2.getAmount(), "CONFIRMATION"))))
//                .toTable(Materialized.as("orders"));
//    }

    @Bean
    public Consumer<ConfirmCommand> confirmations() {
        return input -> orderRepository
                .save(new Order(
                        null,
                        input.getCustomerId(),
                        input.getProductId(),
                        input.getProductCount(),
                        input.getAmount(),
                        "CONFIRMATION"));
    }

    @Bean
    public Function<OrderQuery, OrderQueryResult> queries() {
        return input -> {
            LOG.info("New Query: {}", input.getQueryId());
            List<Order> orders = orderRepository.findByCustomerId(input.getCustomerId());
            return new OrderQueryResult(input.getQueryId(), orders);
        };
    }

}
