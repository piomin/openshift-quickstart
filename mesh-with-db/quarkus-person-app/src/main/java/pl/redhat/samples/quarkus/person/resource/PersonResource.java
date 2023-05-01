package pl.redhat.samples.quarkus.person.resource;

import org.jboss.logging.Logger;
import pl.redhat.samples.quarkus.person.model.Person;
import pl.redhat.samples.quarkus.person.repository.PersonRepository;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import java.util.List;

@Path("/persons")
public class PersonResource {

    @Inject
    Logger log;
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
        log.infof("getPersonById: id=%d", id);
        Person p = personRepository.findById(id);
        log.infof("getPersonById: %s", p);
        return p;
    }

}
