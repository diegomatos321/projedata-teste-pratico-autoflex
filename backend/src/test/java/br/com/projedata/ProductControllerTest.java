package br.com.projedata;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ProductControllerTest {

    @Test
    void shouldCreateProduct() {
        given()
            .contentType("application/json")
            .body("""
                {
                  "code": "P001",
                  "name": "VR Glasses",
                  "price": 2500.00
                }
            """)
        .when()
            .post("/api/products")
        .then()
            .statusCode(201);
    }
}

