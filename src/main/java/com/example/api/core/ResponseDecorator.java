package com.example.api.core;

import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class ResponseDecorator {

    private static final Logger logger = LoggerFactory.getLogger(ResponseDecorator.class);
    private final Response response;

    public ResponseDecorator(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    public ResponseDecorator withLogging() {
        logger.info("Response Status: {}", response.getStatusCode());
        logger.debug("Response Headers: {}", response.getHeaders());
        logger.debug("Response Body: {}", response.getBody().asString());
        return this;
    }

    public ResponseDecorator withTiming() {
        long responseTime = response.getTime();
        logger.info("Response Time: {}ms", responseTime);
        return this;
    }

    public ResponseDecorator withValidation(Function<Response, Boolean> validator) {
        boolean isValid = validator.apply(response);
        if (!isValid) {
            logger.warn("Response validation failed");
        } else {
            logger.debug("Response validation passed");
        }
        return this;
    }

    public ResponseDecorator withErrorHandling() {
        int statusCode = response.getStatusCode();
        if (statusCode >= 400) {
            logger.error("API Error - Status: {}, Body: {}", statusCode, response.getBody().asString());
        }
        return this;
    }

    @SafeVarargs
    public final ResponseDecorator apply(Function<ResponseDecorator, ResponseDecorator>... decorators) {
        ResponseDecorator decorated = this;
        for (Function<ResponseDecorator, ResponseDecorator> decorator : decorators) {
            decorated = decorator.apply(decorated);
        }
        return decorated;
    }

    public static ResponseDecorator decorate(Response response) {
        return new ResponseDecorator(response);
    }
}
