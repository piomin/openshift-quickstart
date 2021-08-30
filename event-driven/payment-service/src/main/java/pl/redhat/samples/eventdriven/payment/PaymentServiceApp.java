package pl.redhat.samples.eventdriven.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.redhat.samples.eventdriven.payment.message.ConfirmCommand;
import pl.redhat.samples.eventdriven.payment.message.OrderCommand;
import pl.redhat.samples.eventdriven.payment.message.OrderEvent;
import pl.redhat.samples.eventdriven.payment.service.PaymentService;

import java.util.function.Consumer;
import java.util.function.Function;

@SpringBootApplication
public class PaymentServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApp.class, args);
    }

    @Autowired
    private PaymentService paymentService;

    @Bean
    public Function<OrderCommand, OrderEvent> orders() {
        return command -> paymentService.reserveBalance(command);
    }

    @Bean
    public Consumer<ConfirmCommand> confirmations() {
        return command -> paymentService.confirmBalance(command);
    }
}
