package pl.redhat.samples.serverless.order.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import pl.redhat.samples.serverless.order.domain.Order;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderRepository implements PanacheRepository<Order> {
}
