# Quarkus Workshops

## Step 1. Local Development

Clone repository from GitHub:
```shell
$ git clone https://github.com/piomin/openshift-quickstart.git
$ cd openshift-quickstart/basic-with-db
```

Run your development IDE (Eclipse/IntelliJ). Import Maven project.

There are two applications `person-app` and `insurance-app`. We will start with `person-app`:
```shell
$ cd person-app
```

Build the application and start it in the dev mode:
```shell
$ mvn quarkus:dev
```

Does the application start successfully? \
Let's fix some problems. Add the configuration properties for database connection only for a development mode:
```properties
%dev.quarkus.datasource.db-kind=h2
%dev.quarkus.datasource.username=sa
%dev.quarkus.datasource.password=password
%dev.quarkus.datasource.jdbc.url=jdbc:h2:mem:testdb
```
The additional dependency need to be included into Maven `pom.xml`:
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-jdbc-h2</artifactId>
</dependency>
```

Your application is available at `localhost:8080`. \
In your web browser run open the following URL: `http://localhost:8080`, then click `/q/dev` context path. You will be redirected to the Quarkus Dev UI Console. \
Let's call example endpoint:
```shell
$ curl http://localhost:8080/persons
```

There is not tables created in H2 database. In `src/main/resources` create `db` directory. Then create the `changeLog.sql` file with the following content:
```text
--liquibase formatted sql

--changeset piomin:1
create table person (
  id serial primary key,
  name varchar(255),
  gender varchar(255),
  age int,
  externalId int
);
insert into person(name, age, gender) values('John Smith', 25, 'MALE');
insert into person(name, age, gender) values('Paul Walker', 65, 'MALE');
insert into person(name, age, gender) values('Lewis Hamilton', 35, 'MALE');
insert into person(name, age, gender) values('Veronica Jones', 20, 'FEMALE');
insert into person(name, age, gender) values('Anne Brown', 60, 'FEMALE');
insert into person(name, age, gender) values('Felicia Scott', 45, 'FEMALE');
```

We will use the Liquibase tool for creating tables with test data. In order to use it, we also need to include the following dependency to the Maven `pom.xml`, which is responsible for integration between Liquibase and Quarkus:
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-liquibase</artifactId>
</dependency>
```

Let's call example endpoint once again:
```shell
$ curl http://localhost:8080/persons
```

Then, we will finish the implementation of `PersonResource`:
```java
@POST
@Transactional
public Person addPerson(Person person) {
    personRepository.persist(person);
    return person;
}

@GET
@Path("/name/{name}")
public List<Person> getPersonsByName(@PathParam("name") String name) {
    return personRepository.findByName(name);
}

@GET
@Path("/{id}")
public Person getPersonById(@PathParam("id") Long id) {
    return personRepository.findById(id);
}

@GET
@Path("/age-greater-than/{age}")
public List<Person> getPersonsByName(@PathParam("age") int age) {
    return personRepository.findByAgeGreaterThan(age);
}
```

Go to the implementation of the Panache repository `PersonRepository` and change it to the following:
```java
@ApplicationScoped
public class PersonRepository implements PanacheRepository<Person> {

    public List<Person> findByName(String name) {
        return find("name", name).list();
    }

    public List<Person> findByAgeGreaterThan(int age) {
        return find("age > ?1", age).list();
    }
}
```

Then call the following endpoints:
```shell
$ curl http://localhost:8080/persons/name/<value-from-changelog>
$ curl http://localhost:8080/persons/age-greater-than/30
```