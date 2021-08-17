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

## Step 3: Communication Between Microservices

Navigate to the `insurance-app`:
```shell
$ cd ../insurance-app
```

Add the dependency with the REST client:
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest-client</artifactId>
</dependency>
```
Create a declarative client responsible for calling the `GET /persons/{id}` endpoint:
```java
@Path("/persons")
@ApplicationScoped
@RegisterRestClient
public interface PersonService {

    @GET
    @Path("/{id}")
    Person getPersonById(@PathParam("id") Long id);
}
```

Go to the `pl/redhat/samples/quarkus/insurance/resource/InsuranceResource.java`. Find the following method:
```java
@GET
@Path("/{id}/details")
public InsuranceDetails getInsuranceDetailsById(@PathParam("id") Long id) {
    return null; // TODO - finish implementation
}
```
Replace it with the following implementation:
```java
@GET
@Path("/{id}/details")
public InsuranceDetails getInsuranceDetailsById(@PathParam("id") Long id) {
    Insurance insurance = insuranceRepository.findById(id);
    InsuranceDetails insuranceDetails = new InsuranceDetails();
    insuranceDetails.personId = insurance.personId;
    insuranceDetails.amount = insurance.amount;
    insuranceDetails.type = insurance.type;
    insuranceDetails.expiry = insurance.expiry;
    insuranceDetails.setPerson(personService.getPersonById(insurance.personId));
    return insuranceDetails;
}
```

Run application in the dev mode. Then run tests using command line. Not all the tests were passed. \
Go to the `pl/redhat/samples/quarkus/insurance/InsuranceResourceTests.java`. Find `newInsuranceAdd()`. This test fails. \

Go to the Maven `pom.xml` and add the following dependency:
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-junit5-mockito</artifactId>
    <scope>test</scope>
</dependency>
```

Back to the `pl/redhat/samples/quarkus/insurance/InsuranceResourceTests.java`. You need to inject `PersonService` into the 
```java
@QuarkusTest
public class InsuranceResourceTests  {

    @InjectMock
    @RestClient
    PersonService personService;
    
    // ... tests
}
```
Go to the `getInsuranceDetailsById()` method inside JUnit test class. Mock communication with `person-app` by adding the following Mockito rule at the beginning of the method:
```java
Mockito.when(personService.getPersonById(Mockito.anyLong())).thenAnswer(invocationOnMock -> {
    Long id = invocationOnMock.getArgument(1, Long.class);
    Person person = new Person();
    person.setId(id);
    person.setAge(33);
    person.setName("Test" + id);
    person.setGender(Gender.FEMALE);
    return person;
});
```
Re-run the tests. All should be passed. Then you can kill the command `mvn quarkus:dev`. 

## Step 4: Deploy to OpenShift

First, login to the cluster using the `oc` client:
```shell
$ oc login -u $USER -p $PASSWORD --server=https://api.ocp1.example.lab:6443
```

Go to: https://console-openshift-console.apps.ocp1.example.lab. \
Switch do the Developer perspective. Click `+Add` -> `Database`. Choose `PostgreSQL`. Set `person-db` as a `Database Service Name`, and leave default values for the rest of fields. \
Do the same for `insurance-app`.

Go to the `person-app` directory in the source code:
```shell
$ cd ../person-app
```

Add the following dependency into Maven `pom.xml`:
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-openshift</artifactId>
</dependency>
```

Open the file `src/main/resources/application.properties`. Verify `quarkus.datasource.*` properties. \
Enable automatic deployment on OpenShift:
```properties
quarkus.container-image.build=true
quarkus.kubernetes.deploy=true
quarkus.kubernetes.deployment-target=openshift
quarkus.kubernetes-client.trust-certs=true
```
Configure a connection with a database. Before, go the OpenShift Web Console and view the `person-db` secret. Then add the following properties:
```properties
quarkus.openshift.env.mapping.postgres_user.from-secret=person-db
quarkus.openshift.env.mapping.postgres_user.with-key=database-user
quarkus.openshift.env.mapping.postgres_password.from-secret=person-db
quarkus.openshift.env.mapping.postgres_password.with-key=database-password
quarkus.openshift.env.mapping.postgres_db.from-secret=person-db
quarkus.openshift.env.mapping.postgres_db.with-key=database-name
```
Then add some optional fields:
```properties
quarkus.openshift.name=person-app
quarkus.openshift.version=${quarkus.application.version}
quarkus.openshift.expose=true
quarkus.openshift.replicas=2
```
The same configuration should be added for `insurance-app`. Just change the name of app and secret.

Run `person-app` application in the dev mode. Go to the Dev UI console. Click `Deploy` on the `OpenShift` tile, then `Deploy` on the next window. Wait and watch on the logs, the build is in progress. \
Go to the OpenShift Web Console. Click `Builds`, choose `person-app` `BuildConfig`. Switch to the `Builds` tab. Choose the latest build. In the `Details` verify `Output to`. \
Then switch to the `Logs` tab. \
Then with `oc` client display a list of routes:
```shell
$ oc get route
```
Copy the path of your `person-app` route. Then call the endpoint e.g.:
```shell
$ curl http://<your_route>/persons
```

Then go back to the code. In your IDE open the file `target/kubernetes/openshift.yml`.

Switch to the `insurance-app` directory:
```shell
$ cd ../insurance-app
```

Execute the following `oc` command to display a list of Kubernetes Services:
```shell
$ oc get svc
```
In the `application.properties` file add the following line:
```properties
pl.redhat.samples.quarkus.insurance.client.PersonService/mp-rest/url=http://person-app:8080
```
Then deploy the app using Maven command:
```shell
$ mvn clean package
```
Once again display a list of routes. Copy the URL of the `insurance-app` route. Call the endpoints:
```shell
$ curl http://<your_route>/insurances
$ curl http://<your_route>/insurances/{id}/details
```

## Step 5: Additional Features

Do for both applications. Add the following dependencies into Maven `pom.xml`:
```shell
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-openapi</artifactId>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-health</artifactId>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-metrics</artifactId>
</dependency>
```
Then add the following lines into the `application.properties` file:
```properties
quarkus.swagger-ui.always-include=true
quarkus.smallrye-health.ui.always-include=true
```
Redeploy the applications. Verify new versions (e.g. liveness and readiness probes). Then in your web browser view the URLs:
```text
http://<your_route>/q/swagger-ui
http://<your_route>/q/health
http://<your_route>/q/health-ui
http://<your_route>/q/health/live
http://<your_route>/q/health/ready
http://<your_route>/q/metrics
```

In Openshift Web Console switch to the `Topology` view. Add the following properties for `person-app` in the `application.properties` file. Then redeploy the app:
```properties
quarkus.openshift.annotation."app.openshift.io/connects-to"=person-db
quarkus.openshift.labels."app.kubernetes.io/part-of"=persons
```
Do the same thing for the `insurance-app`. Move back to the `Topology` view. Add the label `app.kubernetes.io/part-of=persons` for `person-db` DC and `app.kubernetes.io/part-of=insurances` for `insurance-db`.

## Step 6. Using odo

First, list all available components with `odo`.
```shell
$ odo catalog list components
```

We choose S2I with Java.
```shell
$ odo create java-quarkus person-odo
```

Set environment variables for `odo`.
```shell
$ odo config set --env DATABASE_NAME=<your-value> --env DATABASE_USER=<your-value> --env DATABASE_PASSWORD=<your-value>
```

Finally, deploy the application to OpenShift.
```shell
$ odo push
```

Go to the console -> `Topology`. Verify if `person-odo-app` deployment exists. \
Go to `Project` -> `Routes`. Call the route with port `8080`. Call the following endpoint.
```shell
$ curl http://http-8080-person-odo-app-<your_project>.apps.ocp1.example.lab/persons 
[]
```

Then, start development. Run the following command in the `person-app` directory. 
```shell
$ odo watch
```

Finally, you can add a new endpoint and call it via e.g. swagger-ui. Also call your route and verify the result.

## Thanks for participating in workshops!