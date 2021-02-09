package pl.redhat.samples.quarkus.person.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@NoArgsConstructor
@Entity
public class Person extends PanacheEntity {
    private String name;
    private int age;
    private Gender gender;
}
