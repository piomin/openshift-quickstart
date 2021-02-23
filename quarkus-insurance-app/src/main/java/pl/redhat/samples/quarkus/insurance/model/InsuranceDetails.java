package pl.redhat.samples.quarkus.insurance.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.redhat.samples.quarkus.insurance.client.model.Person;

@Data
@NoArgsConstructor
public class InsuranceDetails extends Insurance {
    private Person person;
}
