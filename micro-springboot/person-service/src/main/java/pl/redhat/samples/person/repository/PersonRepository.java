package pl.redhat.samples.person.repository;

import org.springframework.data.repository.CrudRepository;
import pl.redhat.samples.person.domain.Person;

public interface PersonRepository extends CrudRepository<Person, Integer> {
}
