package pl.redhat.samples.serverless.payment.function;

import io.quarkus.funqy.Context;
import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventMapping;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;
import pl.redhat.samples.serverless.payment.domain.Account;
import pl.redhat.samples.serverless.payment.domain.Order;
import pl.redhat.samples.serverless.payment.repository.AccountRepository;

import javax.inject.Inject;

public class OrderReserveFunction {

    @Inject
    Logger log;

    @Inject
    AccountRepository repository;
    @Inject
    @Channel("reserve-events")
    Emitter<Order> orderEmitter;

    @Funq
    public void reserve(Order order) {
        log.infof("Received order: %s", order);
        doReserve(order);
    }

    private void doReserve(Order order) {
        Account account = repository.findById(order.getCustomerId());
        log.infof("Account: %s", account);
        if (order.getStatus().equals("NEW")) {
            account.setReservedAmount(account.getReservedAmount() + order.getAmount());
            account.setCurrentAmount(account.getCurrentAmount() - order.getAmount());
            order.setStatus("IN_PROGRESS");
            log.infof("Order reserved: %s", order);
            orderEmitter.send(order);
        } else if (order.getStatus().equals("CONFIRMED")) {
            account.setReservedAmount(account.getReservedAmount() - order.getAmount());
        }
        repository.persist(account);
    }

}
