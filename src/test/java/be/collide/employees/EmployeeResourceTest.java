package be.collide.employees;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class EmployeeResourceTest {

    private Employee employee;
    @Inject
    DynamoDbClient dynamoDB;


    @BeforeEach
    void setUp() {
        dynamoDB.createTable(CreateTableRequest.builder()
                .tableName("Employees")
                .keySchema(KeySchemaElement.builder()
                        .attributeName("id").keyType(KeyType.HASH).build())
                .attributeDefinitions(AttributeDefinition.builder()
                        .attributeName("id").attributeType("S")
                        .build())
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(10L)
                        .writeCapacityUnits(10L).build())
                .build());
        employee = new Employee();
        employee.setName("Berten De Schutter");
        employee.setCompany("Collide");
    }

    @AfterEach
    void tearDown() {
        dynamoDB.deleteTable(DeleteTableRequest.builder().tableName("Employees").build());
    }

    @Test
    public void addAndRemove() throws InterruptedException {

        String url = given()
                .log().uri()
                .contentType("application/json")
                .body(this.employee)
                .when().post("/employees")
                .then().log().all()
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