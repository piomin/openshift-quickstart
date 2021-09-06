package pl.redhat.samples.serverless.order.function;

import io.quarkus.funqy.Funq;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;
import pl.redhat.samples.serverless.order.domain.Order;
import pl.redhat.samples.serverless.order.repository.OrderRepository;

import javax.inject.Inject;

public class OrderConfirmFunction {

    @Inject
    Logger log;

    @Inject
    OrderRepository repository;
//    @Inject
//    @Channel("reserve-orders")
    Emitter<Order> emitter;

    @Funq
    public void confirm(Order order) {
        log.infof("Confirmed order: %s", order);
//        doConfirm(order);
    }

    private void doConfirm(Order o) {
        Order order = repository.findById(o.getId());
        if (order.getStatus().equals("NEW")) {
            order.setStatus("IN_PROGRESS");
        } else if (order.getStatus().equals("IN_PROGRESS")) {
            order.setStatus("CONFIRMED");
            log.infof("Order confirmed : %s", order);
            emitter.send(order);
        }
        repository.persist(order);
    }
}
