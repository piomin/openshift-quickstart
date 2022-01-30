package pl.redhat.samples.quarkus.insurance.client;

import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import pl.redhat.samples.quarkus.insurance.client.model.Person;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/persons")
@RegisterRestClient
@RegisterClientHeaders(RequestHeaderFactory.class)
public interface PersonService {

    @GET
    @Path("/{id}")
    Person getPersonById(@PathParam("id") Long id);
}
