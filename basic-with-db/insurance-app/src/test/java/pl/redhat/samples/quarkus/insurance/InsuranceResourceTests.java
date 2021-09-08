package pl.redhat.samples.quarkus.insurance;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import pl.redhat.samples.quarkus.insurance.model.Insurance;
import pl.redhat.samples.quarkus.insurance.model.InsuranceType;

import java.time.LocalDate;
import java.sql.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class InsuranceResourceTests {

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

        Insurance insurance = given()
                .pathParam("id", 1)
                .when().get("/insurances/{id}/details")
                .then()
                .statusCode(200)
                .extract()
                .body().as(Insurance.class);
        assertNotNull(insurance);
        assertEquals(1L, insurance.id);
    }

}
