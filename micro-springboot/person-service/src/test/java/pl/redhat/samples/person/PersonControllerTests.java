package pl.redhat.samples.person;

import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.redhat.samples.person.domain.Person;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerTests {

    @LocalServerPort
    int port;

    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.1")
            .withExposedPorts(5432);

    @Test
    @Order(1)
    void add() {
        Person person = Instancio.of(Person.class)
                .ignore(Select.field("id"))
                .create();
        Person saved = webTestClient.post().uri("/persons")
                .bodyValue(person)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Person.class)
                .returnResult().getResponseBody();
        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
    }

    @Test
    @Order(2)
    void updateAndGet() {
        final Integer id = 1;
        Person person = Instancio.of(Person.class)
                .set(Select.field("id"), id)
                .create();
        webTestClient.put().uri("/persons")
                .bodyValue(person)
                .exchange()
                .expectStatus().isOk();
        Person updated = webTestClient.get().uri("/persons/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Person.class)
                .returnResult().getResponseBody();
        Assertions.assertNotNull(updated);
        Assertions.assertNotNull(updated.getId());
        Assertions.assertEquals(id, updated.getId());
    }

    @Test
    @Order(3)
    void getAll() {
        List<Person> persons = webTestClient.get().uri("/persons")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Person.class)
                .hasSize(1)
                .returnResult().getResponseBody();
        Assertions.assertNotNull(persons);
        Assertions.assertEquals(1, persons.size());
    }

    @Test
    @Order(4)
    void deleteAndGet() {
        webTestClient.delete().uri("/persons/{id}", 1)
                .exchange()
                .expectStatus().isOk();
        Person person = webTestClient.get().uri("/persons/{id}", 1)
                .exchange()
                .expectBody(Person.class)
                .returnResult().getResponseBody();
        Assertions.assertNull(person);
    }

}
