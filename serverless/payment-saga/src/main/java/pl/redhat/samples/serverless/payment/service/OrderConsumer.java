package pl.redhat.samples.serverless.payment.service;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;
import pl.redhat.samples.serverless.payment.domain.Order;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

//@ApplicationScoped
public class OrderConsumer {

    @Inject
    Logger log;

//    @Incoming("order-events")
    public void consumeOrder(Order order) {
        log.infof("Received: %s", order);
    }
}
