package pl.redhat.samples.quarkus.insurance.model;

import pl.redhat.samples.quarkus.insurance.client.message.Person;

public class InsuranceDetails extends Insurance {
    private Person person;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
