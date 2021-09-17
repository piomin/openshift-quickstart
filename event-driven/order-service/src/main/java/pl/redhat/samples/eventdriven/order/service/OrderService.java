package pl.redhat.samples.eventdriven.order.service;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import pl.redhat.samples.eventdriven.order.repository.OrderCommandRepository;

@Service
public class OrderService {

    private OrderCommandRepository orderCommandRepository;
    private StreamBridge streamBridge;

    public OrderService(OrderCommandRepository orderCommandRepository, StreamBridge streamBridge) {
        this.orderCommandRepository = orderCommandRepository;
        this.streamBridge = streamBridge;
    }

}
