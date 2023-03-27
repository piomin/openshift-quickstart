package pl.redhat.samples.camel.person;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.redhat.samples.camel.person.domain.Gender;
import pl.redhat.samples.camel.person.domain.Person;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class PersonRouteTests {

    @Container
    static final OracleContainer oracle = new OracleContainer("gvenzl/oracle-xe:21-slim-faststart")
            .withDatabaseName("ORCLPDB1")
            .withUsername("camel")
            .withPassword("qw21QW@!");

    @DynamicPropertySource
    static void oracleProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", oracle::getJdbcUrl);
    }

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void startup() {
        ResponseEntity<String> res = restTemplate.getForEntity("/actuator/health", String.class);
        assertEquals(200, res.getStatusCodeValue());
    }

//    @Test
    void findAll() {
        Person[] persons = restTemplate.getForObject("/persons/", Person[].class);
        assertTrue(persons.length > 0);
    }

//    @Test
    void findById() {
        Person person = restTemplate.getForObject("/persons/{id}", Person.class, 1);
        assertNotNull(person);
        assertEquals(1, person.getId());
    }

//    @Test
    void add() {
        Person p = new Person();
        p.setGender(Gender.MALE);
        p.setName("Test1");
        p.setAge(20);
        p = restTemplate.postForObject("/persons/", p, Person.class);
        assertNotNull(p);
        assertNotNull(p.getId());
    }
}
