package pl.redhat.samples.quarkus.insurance;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.redhat.samples.quarkus.insurance.client.PersonService;
import pl.redhat.samples.quarkus.insurance.client.message.Gender;
import pl.redhat.samples.quarkus.insurance.client.message.Person;
import pl.redhat.samples.quarkus.insurance.model.Insurance;
import pl.redhat.samples.quarkus.insurance.model.InsuranceDetails;
import pl.redhat.samples.quarkus.insurance.model.InsuranceType;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class InsuranceResourceTests {

    @InjectMock
    @RestClient
    PersonService personService;

    @Test
    void getInsurances() {
        List<Insurance> insurances = given().when().get("/insurances")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath().getList(".", Insurance.class);
        assertEquals(insurances.size(), 7);
    }

    @Test
    void getInsuranceById() {
        Insurance insurance = given()
                .pathParam("id", 1)
                .when().get("/insurances/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body().as(Insurance.class);
        assertNotNull(insurance);
        assertEquals(1L, insurance.id);
    }

    @Test
    void newInsuranceAdd() {
        Insurance newInsurance = new Insurance();
        newInsurance.personId = 1L;
        newInsurance.amount = 10000;
        newInsurance.expiry = Date.valueOf(LocalDate.of(2022, 10, 30));
        newInsurance.type = InsuranceType.ACCIDENT;
        Insurance insurance = given()
                .body(newInsurance)
                .contentType(ContentType.JSON)
                .when().post("/insurances")
                .then()
                .statusCode(200)
                .extract()
                .body().as(Insurance.class);
        assertNotNull(insurance);
        assertNotNull(insurance.id);
    }

    @Test
    void getInsuranceDetailsById() {
        Mockito.when(personService.getPersonById(Mockito.anyLong())).thenAnswer(invocationOnMock -> {
            Long id = invocationOnMock.getArgument(0, Long.class);
            Person person = new Person();
            person.setId(id);
            person.setAge(33);
            person.setName("Test" + id);
            person.setGender(Gender.FEMALE);
            return person;
        });

        InsuranceDetails insuranceDetails = given()
                .pathParam("id", 1)
                .when().get("/insurances/{id}/details")
                .then()
                .statusCode(200)
                .extract()
                .body().as(InsuranceDetails.class);
        assertNotNull(insuranceDetails);
        assertEquals(1L, insuranceDetails.getInsurance().id);
    }

}
