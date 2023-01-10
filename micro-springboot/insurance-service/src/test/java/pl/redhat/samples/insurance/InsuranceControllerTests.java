package pl.redhat.samples.insurance;

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
import pl.redhat.samples.insurance.domain.Insurance;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InsuranceControllerTests {

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
        Insurance insurance = Instancio.of(Insurance.class)
                .ignore(Select.field("id"))
                .create();
        insurance = restTemplate.postForObject("/insurances", insurance, Insurance.class);
        Assertions.assertNotNull(insurance);
        Assertions.assertNotNull(insurance.getId());
    }

    @Test
    @Order(2)
    void updateAndGet() {
        final Integer id = 1;
        Insurance insurance = Instancio.of(Insurance.class)
                .set(Select.field("id"), id)
                .create();
        restTemplate.put("/insurances", insurance);
        Insurance updated = restTemplate.getForObject("/insurances/{id}", Insurance.class, id);
        Assertions.assertNotNull(insurance);
        Assertions.assertNotNull(insurance.getId());
        Assertions.assertEquals(id, insurance.getId());
    }

    @Test
    @Order(3)
    void getAll() {
        Insurance[] insurances = restTemplate.getForObject("/insurances", Insurance[].class);
        Assertions.assertEquals(1, insurances.length);
    }

    @Test
    @Order(4)
    void deleteAndGet() {
        restTemplate.delete("/insurances/{id}", 1);
        Insurance insurance = restTemplate.getForObject("/insurances/{id}", Insurance.class, 1);
        Assertions.assertNull(insurance);
    }

}
