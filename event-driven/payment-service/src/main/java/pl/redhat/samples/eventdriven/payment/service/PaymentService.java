package pl.redhat.samples.eventdriven.payment.service;

import org.springframework.stereotype.Service;
import pl.redhat.samples.eventdriven.payment.domain.Account;
import pl.redhat.samples.eventdriven.payment.message.ConfirmCommand;
import pl.redhat.samples.eventdriven.payment.message.OrderCommand;
import pl.redhat.samples.eventdriven.payment.message.OrderEvent;
import pl.redhat.samples.eventdriven.payment.repository.AccountRepository;

@Service
public class PaymentService {

    private AccountRepository accountRepository;

    public PaymentService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public OrderEvent reserveBalance(OrderCommand orderCommand) {
        Account product = accountRepository.findByCustomerId(orderCommand.getCustomerId()).stream().findFirst().orElseThrow();
        product.setReservedAmount(product.getReservedAmount() - orderCommand.getAmount());
        return new OrderEvent(orderCommand.getId(), "OK", "RESERVATION");
    }

    public void confirmBalance(ConfirmCommand confirmCommand) {
        Account product = accountRepository.findByCustomerId(confirmCommand.getCustomerId()).stream().findFirst().orElseThrow();
        product.setCurrentAmount(product.getCurrentAmount() - confirmCommand.getAmount());
    }
}
