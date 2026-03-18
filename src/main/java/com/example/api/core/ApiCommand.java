package com.example.api.core;

import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ApiCommand {

    Response execute();

    String getDescription();

    class GetCommand implements ApiCommand {

        private static final Logger logger = LoggerFactory.getLogger(GetCommand.class);
        private final String endpoint;
        private final io.restassured.specification.RequestSpecification requestSpec;

        public GetCommand(String endpoint, io.restassured.specification.RequestSpecification requestSpec) {
            this.endpoint = endpoint;
            this.requestSpec = requestSpec;
        }

        @Override
        public Response execute() {
            logger.info("Executing GET command: {}", endpoint);
            return io.restassured.RestAssured.given()
                    .spec(requestSpec)
                    .get(endpoint);
        }

        @Override
        public String getDescription() {
            return "GET " + endpoint;
        }
    }

    class PostCommand implements ApiCommand {

        private static final Logger logger = LoggerFactory.getLogger(PostCommand.class);
        private final String endpoint;
        private final Object body;
        private final io.restassured.specification.RequestSpecification requestSpec;

        public PostCommand(String endpoint, Object body, io.restassured.specification.RequestSpecification requestSpec) {
            this.endpoint = endpoint;
            this.body = body;
            this.requestSpec = requestSpec;
        }

        @Override
        public Response execute() {
            logger.info("Executing POST command: {}", endpoint);
            return io.restassured.RestAssured.given()
                    .spec(requestSpec)
                    .body(body)
                    .post(endpoint);
        }

        @Override
        public String getDescription() {
            return "POST " + endpoint;
        }
    }

    class PutCommand implements ApiCommand {

        private static final Logger logger = LoggerFactory.getLogger(PutCommand.class);
        private final String endpoint;
        private final Object body;
        private final io.restassured.specification.RequestSpecification requestSpec;

        public PutCommand(String endpoint, Object body, io.restassured.specification.RequestSpecification requestSpec) {
            this.endpoint = endpoint;
            this.body = body;
            this.requestSpec = requestSpec;
        }

        @Override
        public Response execute() {
            logger.info("Executing PUT command: {}", endpoint);
            return io.restassured.RestAssured.given()
                    .spec(requestSpec)
                    .body(body)
                    .put(endpoint);
        }

        @Override
        public String getDescription() {
            return "PUT " + endpoint;
        }
    }

    class DeleteCommand implements ApiCommand {

        private static final Logger logger = LoggerFactory.getLogger(DeleteCommand.class);
        private final String endpoint;
        private final io.restassured.specification.RequestSpecification requestSpec;

        public DeleteCommand(String endpoint, io.restassured.specification.RequestSpecification requestSpec) {
            this.endpoint = endpoint;
            this.requestSpec = requestSpec;
        }

        @Override
        public Response execute() {
            logger.info("Executing DELETE command: {}", endpoint);
            return io.restassured.RestAssured.given()
                    .spec(requestSpec)
                    .delete(endpoint);
        }

        @Override
        public String getDescription() {
            return "DELETE " + endpoint;
        }
    }
}
