package pl.redhat.samples.eventdriven.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.redhat.samples.eventdriven.payment.service.PaymentService;

@SpringBootApplication
public class PaymentServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApp.class, args);
    }

    @Autowired
    private PaymentService paymentService;

}
