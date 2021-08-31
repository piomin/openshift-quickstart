package pl.redhat.samples.eventdriven.order.repository;

import org.springframework.data.repository.CrudRepository;
import pl.redhat.samples.eventdriven.order.domain.Order;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Integer> {

    List<Order> findByCustomerId(Integer customerId);
}
