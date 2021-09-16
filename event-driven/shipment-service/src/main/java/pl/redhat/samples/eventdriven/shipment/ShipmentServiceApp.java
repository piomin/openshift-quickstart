package pl.redhat.samples.eventdriven.shipment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import pl.redhat.samples.eventdriven.shipment.service.ShipmentService;

@SpringBootApplication
public class ShipmentServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(ShipmentServiceApp.class, args);
    }

    @Autowired
    private ShipmentService shipmentService;
    @Autowired
    private StreamBridge streamBridge;

}
