package pl.redhat.samples.quarkus.insurance;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

import jakarta.ws.rs.core.Application;

@OpenAPIDefinition(info = @Info(title = "Quarkus Insurance Application", version = "1.0"))
public class PersonApplication extends Application {
}
