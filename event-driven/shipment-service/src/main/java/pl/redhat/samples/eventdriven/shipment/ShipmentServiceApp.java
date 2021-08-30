package pl.redhat.samples.eventdriven.shipment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.redhat.samples.eventdriven.shipment.message.ConfirmCommand;
import pl.redhat.samples.eventdriven.shipment.message.OrderCommand;
import pl.redhat.samples.eventdriven.shipment.message.OrderEvent;
import pl.redhat.samples.eventdriven.shipment.service.ShipmentService;

import java.util.function.Consumer;
import java.util.function.Function;

@SpringBootApplication
public class ShipmentServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(ShipmentServiceApp.class, args);
    }

    @Autowired
    private ShipmentService shipmentService;

    @Bean
    public Function<OrderCommand, OrderEvent> orders() {
        return command -> shipmentService.reserveProducts(command);
    }

    @Bean
    public Consumer<ConfirmCommand> confirmations() {
        return command -> shipmentService.confirmProducts(command);
    }
}
