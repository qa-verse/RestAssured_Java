package com.example.api.clients;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UsersClient {

    private final RequestSpecification spec;

    public UsersClient(RequestSpecification spec) {
        this.spec = spec;
    }

    public Response getUser(int userId) {
        return given()
                .spec(spec)
                .basePath("/users")
                .pathParam("userId", userId)
                .get("/{userId}");
    }

    public Response createUser(Object payload) {
        return given()
                .spec(spec)
                .basePath("/users")
                .body(payload)
                .post();
    }
}
