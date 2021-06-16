package pl.redhat.samples.quarkus.person.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class Person extends PanacheEntity {
    public String name;
    public int age;
    @Enumerated(EnumType.STRING)
    public Gender gender;
    public Integer external;
}
