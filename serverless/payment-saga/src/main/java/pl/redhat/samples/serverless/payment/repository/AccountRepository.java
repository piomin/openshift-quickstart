package pl.redhat.samples.serverless.payment.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import pl.redhat.samples.serverless.payment.domain.Account;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccountRepository implements PanacheRepository<Account> {
}
