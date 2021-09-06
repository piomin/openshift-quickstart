package pl.redhat.samples.serverless.order.service;

import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import pl.redhat.samples.serverless.order.domain.Order;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.Random;

@ApplicationScoped
public class OrderPublisher {

    private final Random random = new Random();

    @Outgoing("order-events")
    public Multi<Order> publishOrder() {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(1))
                .map(tick -> {
                    int r = random.nextInt(10000);
                    return new Order(r, r%10+1, r%10+1, 5, 100, "NEW");
                });
    }
}
