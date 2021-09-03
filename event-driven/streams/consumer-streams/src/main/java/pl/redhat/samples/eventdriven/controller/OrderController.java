package pl.redhat.samples.eventdriven.controller;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.redhat.samples.eventdriven.domain.Order;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private InteractiveQueryService queryService;

    public OrderController(InteractiveQueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping
    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        ReadOnlyKeyValueStore<Integer, Order> keyValueStore =
                queryService.getQueryableStore("orders-view", QueryableStoreTypes.keyValueStore());
        KeyValueIterator<Integer, Order> it = keyValueStore.all();
        while (it.hasNext()) {
            KeyValue<Integer, Order> kv = it.next();
            orders.add(kv.value);
        }
        return orders;
    }
}
