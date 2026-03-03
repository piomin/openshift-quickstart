package pl.redhat.samples.insurance;

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
import pl.redhat.samples.insurance.domain.Insurance;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InsuranceControllerTests {

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
        Insurance insurance = Instancio.of(Insurance.class)
                .ignore(Select.field("id"))
                .create();
        Insurance saved = webTestClient.post().uri("/insurances")
                .bodyValue(insurance)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Insurance.class)
                .returnResult().getResponseBody();
        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
    }

    @Test
    @Order(2)
    void updateAndGet() {
        final Integer id = 1;
        Insurance insurance = Instancio.of(Insurance.class)
                .set(Select.field("id"), id)
                .create();
        webTestClient.put().uri("/insurances")
                .bodyValue(insurance)
                .exchange();
        Insurance updated = webTestClient.get().uri("/insurances/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Insurance.class)
                .returnResult().getResponseBody();
        Assertions.assertNotNull(updated);
        Assertions.assertNotNull(updated.getId());
        Assertions.assertEquals(id, updated.getId());
    }

    @Test
    @Order(3)
    void getAll() {
        List<Insurance> insurances = webTestClient.get().uri("/insurances")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Insurance.class)
                .hasSize(8)
                .returnResult().getResponseBody();
        Assertions.assertNotNull(insurances);
        Assertions.assertEquals(8, insurances.size());
    }

    @Test
    @Order(4)
    void deleteAndGet() {
        webTestClient.delete().uri("/insurances/{id}", 1)
                .exchange()
                .expectStatus().isOk();
        Insurance insurance = webTestClient.get().uri("/insurances/{id}", 1)
                .exchange()
                .expectBody(Insurance.class)
                .returnResult().getResponseBody();
        Assertions.assertNull(insurance);
    }

}
