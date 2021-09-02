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

    InteractiveQueryService queryService;

    public OrderController(InteractiveQueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping
    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        ReadOnlyKeyValueStore<Integer, String> keyValueStore =
                queryService.getQueryableStore("incoming-stream", QueryableStoreTypes.keyValueStore());
        KeyValueIterator<Integer, String> it = keyValueStore.all();
        while (it.hasNext()) {
            KeyValue<Integer, String> kv = it.next();
            orders.add(new Order(kv.key, kv.value, null));
        }
        return orders;
    }
}
