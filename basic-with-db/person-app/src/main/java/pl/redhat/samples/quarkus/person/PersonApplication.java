package pl.redhat.samples.quarkus.person;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(info = @Info(title = "Quarkus Person Application", version = "1.0"))
public class PersonApplication extends Application {
}
