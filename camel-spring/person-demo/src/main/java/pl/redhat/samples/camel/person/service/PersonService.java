package pl.redhat.samples.camel.person.service;

import org.springframework.stereotype.Service;
import pl.redhat.samples.camel.person.domain.Person;
import pl.redhat.samples.camel.person.repository.PersonRepository;

import java.util.List;

@Service
public class PersonService {

    private PersonRepository repository;

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    public List<Person> findAll() {
        return (List<Person>) repository.findAll();
    }

    public Person findById(Integer id) {
        return repository.findById(id).orElseThrow();
    }

    public int countOlderThan(int ageIn) {
        return repository.countOlderThen(ageIn);
    }

    public Person add(Person person) {
        return repository.save(person);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

}
