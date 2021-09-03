package pl.redhat.samples.serverless.payment.domain.deserialize;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import pl.redhat.samples.serverless.payment.domain.Order;

public class OrderDeserializer extends ObjectMapperDeserializer<Order> {

    public OrderDeserializer() {
        super(Order.class);
    }

}
