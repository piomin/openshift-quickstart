package pl.redhat.samples.eventdriven.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.redhat.samples.eventdriven.gateway.message.Order;
import pl.redhat.samples.eventdriven.gateway.message.OrderQueryResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@SpringBootApplication
public class EventGatewayApp {

    public static void main(String[] args) {
        SpringApplication.run(EventGatewayApp.class, args);
    }

    @Bean
    public Consumer<OrderQueryResult> queries() {
        return input -> results().put(input.getQueryId(), input.getOrders());
    }

    @Bean
    Map<String, List<Order>> results() {
        return new HashMap();
    }
}
