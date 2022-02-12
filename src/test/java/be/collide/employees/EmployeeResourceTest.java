package be.collide.employees;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class EmployeeResourceTest {

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setName("Berten De Schutter");
        employee.setCompany("Collide");
    }

    @Test
    public void addAndRemove() {

        String url = given()
                .contentType("application/json")
                .body(this.employee)
                .when().post("/employees")
                .then()
                .statusCode(201)
                .extract().headers().get("Location").getValue();

        ValidatableResponse validatableResponse = given()
                .when().get(url)
                .then()
                .statusCode(200);
        assertNotNull(validatableResponse.extract().body().jsonPath().get("id"));
        assertEquals(validatableResponse.extract().body().jsonPath().get("name"), employee.getName());
        assertEquals(validatableResponse.extract().body().jsonPath().get("company"), employee.getCompany());

        Employee retrievedEmployee = validatableResponse.extract().body().as(Employee.class);

        retrievedEmployee.setName("Philip Boermans");
        given()
                .contentType("application/json")
                .body(retrievedEmployee)
                .when().put(url)
                .then()
                .statusCode(204);

        validatableResponse = given()
                .when().get(url)
                .then()
                .statusCode(200);
        assertEquals(validatableResponse.extract().body().jsonPath().get("name"), retrievedEmployee.getName());

        given().when().delete(url).then().statusCode(204);

        given()
                .when().get(url)
                .then()
                .statusCode(404);

    }
}