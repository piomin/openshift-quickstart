package pl.redhat.samples.eventdriven.payment.repository;

import org.springframework.data.repository.CrudRepository;
import pl.redhat.samples.eventdriven.payment.domain.Account;

import java.util.List;

public interface AccountRepository extends CrudRepository<Account, Integer> {

    List<Account> findByCustomerId(Integer customerId);
}
