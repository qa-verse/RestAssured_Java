package com.example.api.tests;

import com.example.api.core.BaseApiTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class HealthCheckTest extends BaseApiTest {

    @Test(groups = "smoke")
    public void healthEndpointShouldBeReachable() {
        given()
                .spec(requestSpec)
                .get(config.healthEndpoint())
                .then()
                .statusCode(200)
                .body("id", equalTo(config.healthExpectedId()));
    }
}
