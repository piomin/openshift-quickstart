package pl.redhat.samples.eventdriven.gateway.controller;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.redhat.samples.eventdriven.gateway.message.OrderCommand;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private StreamBridge streamBridge;

    public OrderController(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @PostMapping
    public Boolean orders(@RequestBody OrderCommand orderCommand) {
        orderCommand.setId(UUID.randomUUID().toString());
        return streamBridge.send("orders-out-0", orderCommand);
    }
}
