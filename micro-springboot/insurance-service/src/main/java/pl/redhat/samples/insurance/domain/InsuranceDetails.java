package pl.redhat.samples.insurance.domain;

import pl.redhat.samples.insurance.client.message.Person;

public class InsuranceDetails extends Insurance {
    private Person person;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
