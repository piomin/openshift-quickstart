package pl.redhat.samples.eventdriven.shipment.repository;

import org.springframework.data.repository.CrudRepository;
import pl.redhat.samples.eventdriven.shipment.domain.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {
}
