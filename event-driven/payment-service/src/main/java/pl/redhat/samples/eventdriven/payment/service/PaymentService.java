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
        Account account = accountRepository.findByCustomerId(orderCommand.getCustomerId()).stream().findFirst().orElseThrow();
        account.setReservedAmount(account.getReservedAmount() - orderCommand.getAmount());
        if (account.getReservedAmount() > 0) {
            return new OrderEvent(orderCommand.getId(), "RESERVATION", "FAILED");
        } else {
            accountRepository.save(account);
            return new OrderEvent(orderCommand.getId(), "RESERVATION", "OK");
        }
    }

    public void confirmBalance(ConfirmCommand confirmCommand) {
        Account product = accountRepository.findByCustomerId(confirmCommand.getCustomerId()).stream().findFirst().orElseThrow();
        product.setCurrentAmount(product.getCurrentAmount() - confirmCommand.getAmount());
    }
}
