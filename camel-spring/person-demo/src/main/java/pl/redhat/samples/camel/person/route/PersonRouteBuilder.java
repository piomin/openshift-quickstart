package pl.redhat.samples.camel.person.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;
import pl.redhat.samples.camel.person.domain.Person;
import pl.redhat.samples.camel.person.service.PersonService;

@Component
public class PersonRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .contextPath("/camel")
                .apiContextPath("/v3/api-docs")
                .apiProperty("api.title", "Person Management API")
                .apiProperty("api.version", "1.0")
                .apiContextRouteId("doc-api")
                .port(8080)
                .bindingMode(RestBindingMode.json);

        rest("/persons")
            .get("/{id}").to("direct:findById")
            .get("/").to("direct:findAll")
            .get("/older-than/{age}").to("direct:countOlderThan")
            .post().consumes("application/json").type(Person.class)
                .to("direct:add")
            .delete("/{id}").to("direct:delete");

        from("direct:findById").bean(PersonService.class, "findById(${header.id})");
        from("direct:findAll").bean(PersonService.class, "findAll");
        from("direct:countOlderThan").bean(PersonService.class, "countOlderThan(${header.age})");
        from("direct:add")
                .bean(PersonService.class, "add(${body})")
                .log("New: ${body}");
        from("direct:delete").bean(PersonService.class, "delete(${header.id})");
    }
}
