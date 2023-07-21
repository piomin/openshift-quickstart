package pl.redhat.samples.quarkus.person;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.redhat.samples.quarkus.person.model.Gender;
import pl.redhat.samples.quarkus.person.model.Person;
import pl.redhat.samples.quarkus.person.repository.PersonRepository;

import jakarta.inject.Inject;

@QuarkusTest
@TestTransaction
public class PersonRepositoryTests {

    @Inject
    PersonRepository personRepository;

    @Test
    void addPerson() {
        Person newPerson = new Person();
        newPerson.age = 22;
        newPerson.name = "TestNew";
        newPerson.gender = Gender.FEMALE;
        personRepository.persist(newPerson);
        Assertions.assertNotNull(newPerson.id);
    }
}
