package pl.redhat.samples.eventdriven.payment.service;

import org.springframework.stereotype.Service;
import pl.redhat.samples.eventdriven.payment.repository.AccountRepository;

@Service
public class PaymentService {

    private AccountRepository accountRepository;

    public PaymentService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

}
