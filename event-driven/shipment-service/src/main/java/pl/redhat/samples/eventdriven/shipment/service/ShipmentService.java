package pl.redhat.samples.eventdriven.shipment.service;

import org.springframework.stereotype.Service;
import pl.redhat.samples.eventdriven.shipment.repository.ProductRepository;

@Service
public class ShipmentService {

    private ProductRepository productRepository;

    public ShipmentService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

}
