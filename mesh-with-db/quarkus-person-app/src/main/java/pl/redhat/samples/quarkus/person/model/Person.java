package pl.redhat.samples.quarkus.person.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
public class Person extends PanacheEntity {
    private String name;
    private int age;
    @Enumerated(EnumType.STRING)
    private Gender gender;
//    TODO - uncomment to enable v2
    private Integer externalId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getExternalId() {
        return externalId;
    }

    public void setExternalId(Integer externalId) {
        this.externalId = externalId;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", externalID=" + externalId +
                ", id=" + id +
                '}';
    }
}
