package pl.redhat.samples.eventdriven.order.repository;

import org.springframework.data.repository.CrudRepository;
import pl.redhat.samples.eventdriven.order.message.OrderCommand;

public interface OrderCommandRepository extends CrudRepository<OrderCommand, String> {
}
