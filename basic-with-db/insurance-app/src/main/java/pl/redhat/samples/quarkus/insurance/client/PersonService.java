package pl.redhat.samples.quarkus.insurance.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import pl.redhat.samples.quarkus.insurance.client.message.Person;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/persons")
@RegisterRestClient
public interface PersonService {

    @GET
    @Path("/{id}")
    Person getPersonById(@PathParam("id") Long id);
}
