package pl.redhat.samples.quarkus.person.resource;

import pl.redhat.samples.quarkus.person.model.Person;
import pl.redhat.samples.quarkus.person.repository.PersonRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import java.util.List;

@Path("/persons")
public class PersonResource {

    @Inject
    PersonRepository personRepository;

    @POST
    @Transactional
    public Person addPerson(Person person) {
        personRepository.persist(person);
        return person;
    }

    @GET
    public List<Person> getPersons() {
        return personRepository.listAll();
    }

    @GET
    @Path("/{id}")
    public Person getPersonById(@PathParam("id") Long id) {
        return personRepository.findById(id);
    }

}
