package pl.redhat.samples.serverless.order.service;

import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.ce.OutgoingCloudEventMetadata;
import io.smallrye.reactive.messaging.ce.OutgoingCloudEventMetadataBuilder;
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import pl.redhat.samples.serverless.order.domain.Order;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.Random;

@ApplicationScoped
public class OrderPublisher {

    private final Random random = new Random();

//    @Outgoing("order-events")
//    public Multi<Order> publishOrder() {
//        return Multi.createFrom().ticks().every(Duration.ofSeconds(1))
//                .map(tick -> {
//                    long r = random.nextInt(10000);
//                    return new Order(r, r%10+1, r%10+1, 5, 100, "NEW");
//                });
//    }

    @Outgoing("order-events")
    public Multi<Message<Order>> publishOrder() {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(1))
                .map(tick -> {
                    long r = random.nextInt(10000);
                    return Message.of(new Order(r, r%10+1, r%10+1, 5, 100, "NEW"))
                            .addMetadata(OutgoingKafkaRecordMetadata.<String>builder()
                                    .withHeaders(new RecordHeaders().add("X-Routing-Name", "reserve".getBytes()))
                                    .build());
                });
    }
}
