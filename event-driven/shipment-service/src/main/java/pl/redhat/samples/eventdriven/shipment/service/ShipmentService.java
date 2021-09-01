package pl.redhat.samples.eventdriven.shipment.service;

import org.springframework.stereotype.Service;
import pl.redhat.samples.eventdriven.shipment.domain.Product;
import pl.redhat.samples.eventdriven.shipment.exception.NotEnoughProductsException;
import pl.redhat.samples.eventdriven.shipment.message.ConfirmCommand;
import pl.redhat.samples.eventdriven.shipment.message.OrderCommand;
import pl.redhat.samples.eventdriven.shipment.message.OrderEvent;
import pl.redhat.samples.eventdriven.shipment.repository.ProductRepository;

@Service
public class ShipmentService {

    private ProductRepository productRepository;

    public ShipmentService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public OrderEvent reserveProducts(OrderCommand orderCommand) {
        Product product = productRepository.findById(orderCommand.getProductId()).orElseThrow();
        product.setReservedCount(product.getReservedCount() - orderCommand.getProductCount());
        if (product.getReservedCount() > 0)
            throw new NotEnoughProductsException();
        productRepository.save(product);
        return new OrderEvent(orderCommand.getId(), "OK", "RESERVATION");
    }

    public OrderEvent confirmProducts(ConfirmCommand confirmCommand) {
        Product product = productRepository.findById(confirmCommand.getProductId()).orElseThrow();
        product.setReservedCount(product.getCurrentCount() - confirmCommand.getProductCount());
        productRepository.save(product);
        return new OrderEvent(confirmCommand.getOrderId(), "OK", "CONFIRMATION");
    }
}
