package pl.redhat.samples.eventdriven.shipment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.redhat.samples.eventdriven.shipment.message.ConfirmCommand;
import pl.redhat.samples.eventdriven.shipment.message.OrderCommand;
import pl.redhat.samples.eventdriven.shipment.message.OrderCommandDelayed;
import pl.redhat.samples.eventdriven.shipment.message.OrderEvent;
import pl.redhat.samples.eventdriven.shipment.service.ShipmentService;

import java.util.concurrent.DelayQueue;
import java.util.function.Consumer;
import java.util.function.Function;

@SpringBootApplication
@EnableScheduling
public class ShipmentServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(ShipmentServiceApp.class, args);
    }

    @Autowired
    private ShipmentService shipmentService;
    @Autowired
    private StreamBridge streamBridge;

    @Bean
    public Function<OrderCommand, OrderEvent> orders() {
        return command -> shipmentService.reserveProducts(command);
    }

    @Bean
    public Consumer<ConfirmCommand> confirmations() {
        return command -> shipmentService.confirmProducts(command);
    }

    @Bean
    public DelayQueue<OrderCommandDelayed> delayQueue() {
        return new DelayQueue<OrderCommandDelayed>();
    }

    @Bean
    public Consumer<OrderCommand> dlqs() {
        return input -> delayQueue().offer(new OrderCommandDelayed(input, 60000));
    }

}
