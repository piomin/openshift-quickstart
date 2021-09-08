package pl.redhat.samples.quarkus.insurance.model;

import pl.redhat.samples.quarkus.insurance.client.message.Person;

public class InsuranceDetails extends Insurance {
    private Person person;

    public InsuranceDetails(Person person, Insurance insurance) {
        this.person = person;
        amount = insurance.amount;
        expiry = insurance.expiry;
        id = insurance.id;
        type = insurance.type;
        personId = insurance.personId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
