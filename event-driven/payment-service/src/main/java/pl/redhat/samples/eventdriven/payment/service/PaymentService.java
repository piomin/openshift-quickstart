package pl.redhat.samples.eventdriven.payment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.redhat.samples.eventdriven.payment.domain.Account;
import pl.redhat.samples.eventdriven.payment.message.ConfirmCommand;
import pl.redhat.samples.eventdriven.payment.message.OrderCommand;
import pl.redhat.samples.eventdriven.payment.message.OrderEvent;
import pl.redhat.samples.eventdriven.payment.repository.AccountRepository;

@Service
public class PaymentService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentService.class);

    private AccountRepository accountRepository;

    public PaymentService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public OrderEvent reserveBalance(OrderCommand orderCommand) {
        Account account = accountRepository.findByCustomerId(orderCommand.getCustomerId()).stream().findFirst().orElseThrow();
        account.setReservedAmount(account.getReservedAmount() - orderCommand.getAmount());
        if (account.getReservedAmount() < 0) {
            return new OrderEvent(orderCommand.getId(), "RESERVATION", "FAILED", "payment-service");
        } else {
            accountRepository.save(account);
            LOG.info("Balance reserved: id={}, orderId={} ", account.getId(), orderCommand.getId());
            return new OrderEvent(orderCommand.getId(), "RESERVATION", "OK", "payment-service");
        }
    }

    public void confirmBalance(ConfirmCommand confirmCommand) {
        Account account = accountRepository.findByCustomerId(confirmCommand.getCustomerId()).stream().findFirst().orElseThrow();
        account.setCurrentAmount(account.getCurrentAmount() - confirmCommand.getAmount());
        accountRepository.save(account);
        LOG.info("Balance confirmed: account={}, orderId={}", account, confirmCommand);
    }

}
