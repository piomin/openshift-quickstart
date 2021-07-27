package pl.redhat.samples.person.controller;

import org.springframework.web.bind.annotation.*;
import pl.redhat.samples.person.domain.Person;
import pl.redhat.samples.person.repository.PersonRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private PersonRepository repository;

    public PersonController(PersonRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Person> getAll() {
        // TODO - implement
        return new ArrayList<>(0);
    }

    @GetMapping("/{id}")
    public Person getById(@PathVariable("id") Integer id) {
        // TODO - implement
        return null;
    }

    @PostMapping
    public Person addNew(@RequestBody Person person) {
        // TODO - implement
        return null;
    }
}
