package pl.redhat.samples.eventdriven.gateway.controller;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.*;
import pl.redhat.samples.eventdriven.gateway.message.Order;
import pl.redhat.samples.eventdriven.gateway.message.OrderCommand;
import pl.redhat.samples.eventdriven.gateway.message.OrderQuery;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private StreamBridge streamBridge;
    private Map<String, List<Order>> results;

    public OrderController(StreamBridge streamBridge, Map<String, List<Order>> results) {
        this.streamBridge = streamBridge;
        this.results = results;
    }

    @PostMapping
    public Boolean orders(@RequestBody OrderCommand orderCommand) {
        orderCommand.setId(UUID.randomUUID().toString());
        return streamBridge.send("orders-out-0", orderCommand);
    }

    @GetMapping("/customer/{customerId}")
    public String query(@PathVariable Integer customerId) {
        String uuid = UUID.randomUUID().toString();
        streamBridge.send("queries-out-0", new OrderQuery(uuid, customerId));
        return uuid;
    }

    @GetMapping("/results/{queryId}")
    public List<Order> orders(@PathVariable String queryId) {
        return results.remove(queryId);
    }
}
