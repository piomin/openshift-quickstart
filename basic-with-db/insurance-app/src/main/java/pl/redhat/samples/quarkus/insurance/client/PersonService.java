package pl.redhat.samples.quarkus.insurance.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import pl.redhat.samples.quarkus.insurance.client.message.Person;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("/persons")
@ApplicationScoped
@RegisterRestClient(configKey = "person")
public interface PersonService {

    @GET
    @Path("/{id}")
    Person getPersonById(@PathParam("id") Long id);
}
