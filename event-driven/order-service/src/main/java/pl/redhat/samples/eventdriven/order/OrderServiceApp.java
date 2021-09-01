package pl.redhat.samples.eventdriven.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.redhat.samples.eventdriven.order.message.OrderCommand;
import pl.redhat.samples.eventdriven.order.message.OrderEvent;
import pl.redhat.samples.eventdriven.order.service.OrderService;

import java.util.function.Consumer;

@SpringBootApplication
public class OrderServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApp.class, args);
    }

    @Autowired
    OrderService orderService;

    @Bean
    public Consumer<OrderCommand> orders() {
        return command -> orderService.addOrderCommand(command);
    }

    @Bean
    public Consumer<OrderEvent> events() {
        return event -> orderService.updateOrderCommandStatus(event.getCommandId());
    }

    @Bean
    public Consumer<OrderEvent> failedEvents() {
        // TODO - add implementation
        return event -> orderService.updateOrderCommandStatus(event.getCommandId());
    }
}
