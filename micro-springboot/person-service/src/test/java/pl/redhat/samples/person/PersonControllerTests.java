package pl.redhat.samples.person;

import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.redhat.samples.person.domain.Person;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.1")
            .withExposedPorts(5432);

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    @Order(1)
    void add() {
        Person person = Instancio.of(Person.class)
                .ignore(Select.field("id"))
                .create();
        person = restTemplate.postForObject("/persons", person, Person.class);
        Assertions.assertNotNull(person);
        Assertions.assertNotNull(person.getId());
    }

    @Test
    @Order(2)
    void updateAndGet() {
        final Integer id = 1;
        Person person = Instancio.of(Person.class)
                .set(Select.field("id"), id)
                .create();
        restTemplate.put("/persons", person);
        Person updated = restTemplate.getForObject("/persons/{id}", Person.class, id);
        Assertions.assertNotNull(updated);
        Assertions.assertNotNull(updated.getId());
        Assertions.assertEquals(id, updated.getId());
    }

    @Test
    @Order(3)
    void getAll() {
        Person[] persons = restTemplate.getForObject("/persons", Person[].class);
        Assertions.assertEquals(1, persons.length);
    }

    @Test
    @Order(4)
    void deleteAndGet() {
        restTemplate.delete("/persons/{id}", 1);
        Person person = restTemplate.getForObject("/persons/{id}", Person.class, 1);
        Assertions.assertNull(person);
    }

}
