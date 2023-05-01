package pl.redhat.samples.quarkus.person.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import pl.redhat.samples.quarkus.person.model.Person;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PersonRepository implements PanacheRepository<Person> {
}
