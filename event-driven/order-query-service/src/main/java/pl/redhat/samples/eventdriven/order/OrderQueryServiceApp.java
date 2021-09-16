package pl.redhat.samples.eventdriven.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.redhat.samples.eventdriven.order.repository.OrderRepository;

@SpringBootApplication
public class OrderQueryServiceApp {

    private static final Logger LOG = LoggerFactory.getLogger(OrderQueryServiceApp.class);

    public static void main(String[] args) {
        SpringApplication.run(OrderQueryServiceApp.class, args);
    }

    @Autowired
    OrderRepository orderRepository;

}
