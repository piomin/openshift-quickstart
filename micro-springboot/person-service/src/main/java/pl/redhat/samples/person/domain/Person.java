package pl.redhat.samples.person.domain;

//@Entity
public class Person {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    public String name;
    public int age;
//    @Enumerated(EnumType.STRING)
    public Gender gender;
    public Integer externalId;
}
