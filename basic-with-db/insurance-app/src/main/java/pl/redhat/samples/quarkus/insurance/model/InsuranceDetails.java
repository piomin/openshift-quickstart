package pl.redhat.samples.quarkus.insurance.model;

import pl.redhat.samples.quarkus.insurance.client.message.Person;

public class InsuranceDetails {
    private Person person;
    private Insurance insurance;

    public InsuranceDetails(Person person, Insurance insurance) {
        this.person = person;
        this.insurance = insurance;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Insurance getInsurance() {
        return insurance;
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }
}
