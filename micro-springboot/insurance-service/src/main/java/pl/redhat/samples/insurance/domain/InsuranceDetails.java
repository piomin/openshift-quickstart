package pl.redhat.samples.insurance.domain;

import pl.redhat.samples.insurance.client.message.Person;

public class InsuranceDetails extends Insurance {
    private Person person;

    public InsuranceDetails(Person person, Insurance insurance) {
        this.person = person;
        setAmount(insurance.getAmount());
        setExpiry(insurance.getExpiry());
        setId(insurance.getId());
        setType(insurance.getType());
        setPersonId(insurance.getPersonId());
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
