package pl.redhat.samples.person.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pl.redhat.samples.person.domain.Person;
import pl.redhat.samples.person.repository.PersonRepository;

import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private static final Logger LOG = LoggerFactory.getLogger(PersonController.class);
    private PersonRepository repository;

    public PersonController(PersonRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Person> getAll() {
        LOG.info("Get all persons");
        return (List<Person>) repository.findAll();
    }

    @GetMapping("/{id}")
    public Person getById(@PathVariable("id") Integer id) {
        LOG.info("Get person by id={}", id);
        return repository.findById(id).orElseThrow();
    }

    @PostMapping
    public Person addNew(@RequestBody Person person) {
        LOG.info("Add new person: {}", person);
        return repository.save(person);
    }
}
