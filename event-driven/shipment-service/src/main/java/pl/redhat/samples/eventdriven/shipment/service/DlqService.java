package pl.redhat.samples.eventdriven.shipment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.redhat.samples.eventdriven.shipment.ShipmentServiceApp;
import pl.redhat.samples.eventdriven.shipment.exception.NotEnoughProductsException;
import pl.redhat.samples.eventdriven.shipment.message.OrderCommandDelayed;
import pl.redhat.samples.eventdriven.shipment.message.OrderEvent;

import java.util.concurrent.DelayQueue;

@Service
public class DlqService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentServiceApp.class);

    private DelayQueue<OrderCommandDelayed> delayQueue;
    private StreamBridge streamBridge;
    private ShipmentService shipmentService;

    public DlqService(DelayQueue<OrderCommandDelayed> delayQueue, StreamBridge streamBridge, ShipmentService shipmentService) {
        this.delayQueue = delayQueue;
        this.streamBridge = streamBridge;
        this.shipmentService = shipmentService;
    }

    @Scheduled(fixedDelay = 1000)
    public void schedule() {
        try {
            OrderCommandDelayed delayed = delayQueue.take();
            OrderEvent event = null;
            try {
                shipmentService.reserveProducts(delayed.getOrderCommand());
                event = new OrderEvent(delayed.getOrderCommand().getId(), "RESERVATION", "OK");
            } catch (NotEnoughProductsException e) {
                LOG.error("Not enough products: id={}", delayed.getOrderCommand().getProductId());
                event = new OrderEvent(delayed.getOrderCommand().getId(), "RESERVATION", "FAILED");
            }
            streamBridge.send("orders-out-0", event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
