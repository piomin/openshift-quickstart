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

## Step 2. Local Testing

Let's kill the `mvn quarkus:dev` command for a moment. Add the following dependencies to the Maven `pom.xml`:
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-junit5</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <scope>test</scope>
</dependency>
```

Then create a package `pl.redhat.samples.quarkus.person` and the class `PersonResourceTests` inside that package:
```java
@QuarkusTest
public class PersonResourceTests {

    @Test
    void getPersons() {
        List<Person> persons = given().when().get("/persons")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath().getList(".", Person.class);
        assertEquals(persons.size(), 6);
    }

    @Test
    void getPersonById() {
        Person person = given()
                .pathParam("id", 1)
                .when().get("/persons/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body().as(Person.class);
        assertNotNull(person);
        assertEquals(1L, person.id);
    }

    @Test
    void newPersonAdd() {
        Person newPerson = new Person();
        newPerson.age = 22;
        newPerson.name = "TestNew";
        newPerson.gender = Gender.FEMALE;
        Person person = given()
                .body(newPerson)
                .when().post("/persons")
                .then()
                .statusCode(200)
                .extract()
                .body().as(Person.class);
        assertNotNull(person);
        assertNotNull(person.id);
    }
}
```

Run the command `mvn quarkus:dev` once again. You should see:
```text
Tests paused
Press [r] to resume testing, [o] Toggle test output, [h] for more options>
```
Type `r`. You should have 2 (or 1)/3 tests succeeded. Go to the dev UI console. Find `Test results` icon and click it. See the output for the failed tests. Try to fix them. \
Especially verify the `newPersonAdd` test. Maybe you should add something after the `.body(newPerson)` line? Then type `r` once again. \
Kill the `mvn quarkus:dev` command. 

Navigate to the `insurance-app`:
```shell
$ cd ../insurance-app
```

Run application in the dev mode. Then run tests using command line. Not all the tests were passed. Go to the Maven `pom.xml` and add the following dependency:
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-junit5-mockito</artifactId>
    <scope>test</scope>
</dependency>
```

Then go to the `pl/redhat/samples/quarkus/insurance/InsuranceResourceTests.java`. Find `newInsuranceAdd()`. This test fails. \
Go to the `pl/redhat/samples/quarkus/insurance/resource/InsuranceResource.java`. Find the following method:
```java
@GET
@Path("/{id}/details")
public InsuranceDetails getInsuranceDetailsById(@PathParam("id") Long id) {
    return null; // TODO - finish implementation
}
```

```java
@QuarkusTest
public class InsuranceResourceTests {

    @InjectMock
    @RestClient
    PersonService personService;
    
    // ... tests
}
```