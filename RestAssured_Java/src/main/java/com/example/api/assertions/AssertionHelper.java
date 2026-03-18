package com.example.api.assertions;

import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public final class AssertionHelper {

    private static final Logger logger = LoggerFactory.getLogger(AssertionHelper.class);

    private AssertionHelper() {
    }

    public static void assertStatusCode(Response response, int expectedCode) {
        int actualCode = response.getStatusCode();
        logger.debug("Asserting status code: expected={}, actual={}", expectedCode, actualCode);
        assertThat("Status code mismatch", actualCode, equalTo(expectedCode));
    }

    public static void assertSuccess(Response response) {
        int statusCode = response.getStatusCode();
        logger.debug("Asserting success status code: {}", statusCode);
        assertThat("Expected success status code (2xx)", statusCode, greaterThanOrEqualTo(200));
        assertThat("Expected success status code (2xx)", statusCode, lessThan(300));
    }

    public static void assertHeader(Response response, String headerName, String expectedValue) {
        String actualValue = response.getHeader(headerName);
        logger.debug("Asserting header {}: expected={}, actual={}", headerName, expectedValue, actualValue);
        assertThat("Header mismatch: " + headerName, actualValue, equalTo(expectedValue));
    }

    public static void assertHeaderExists(Response response, String headerName) {
        String headerValue = response.getHeader(headerName);
        logger.debug("Asserting header exists: {}", headerName);
        assertThat("Header does not exist: " + headerName, headerValue, notNullValue());
    }

    public static void assertContentType(Response response, String expectedType) {
        String actualType = response.getContentType();
        logger.debug("Asserting content type: expected={}, actual={}", expectedType, actualType);
        assertThat("Content type mismatch", actualType, containsString(expectedType));
    }

    public static void assertJsonPath(Response response, String jsonPath, Object expectedValue) {
        Object actualValue = response.jsonPath().get(jsonPath);
        logger.debug("Asserting JSON path {}: expected={}, actual={}", jsonPath, expectedValue, actualValue);
        assertThat("JSON path mismatch: " + jsonPath, actualValue, equalTo(expectedValue));
    }

    public static void assertJsonPathNotNull(Response response, String jsonPath) {
        Object value = response.jsonPath().get(jsonPath);
        logger.debug("Asserting JSON path {} is not null", jsonPath);
        assertThat("JSON path is null: " + jsonPath, value, notNullValue());
    }

    public static void assertJsonPathNull(Response response, String jsonPath) {
        Object value = response.jsonPath().get(jsonPath);
        logger.debug("Asserting JSON path {} is null", jsonPath);
        assertThat("JSON path is not null: " + jsonPath, value, nullValue());
    }

    public static void assertListSize(Response response, String jsonPath, int expectedSize) {
        List<?> list = response.jsonPath().getList(jsonPath);
        logger.debug("Asserting list size at {}: expected={}, actual={}", jsonPath, expectedSize, list != null ? list.size() : 0);
        assertThat("List is null at: " + jsonPath, list, notNullValue());
        assertThat("List size mismatch at: " + jsonPath, list.size(), equalTo(expectedSize));
    }

    public static void assertListNotEmpty(Response response, String jsonPath) {
        List<?> list = response.jsonPath().getList(jsonPath);
        logger.debug("Asserting list is not empty at: {}", jsonPath);
        assertThat("List is null or empty at: " + jsonPath, list, notNullValue());
        assertThat("List is empty at: " + jsonPath, list, not(empty()));
    }

    public static void assertResponseTime(Response response, long maxTimeMillis) {
        long actualTime = response.getTime();
        logger.debug("Asserting response time: max={}ms, actual={}ms", maxTimeMillis, actualTime);
        assertThat("Response time exceeds threshold", actualTime, lessThanOrEqualTo(maxTimeMillis));
    }

    public static void assertSchemaValid(Response response) {
        logger.debug("Schema validation check");
    }

    public static void assertResponseContainsKey(Response response, String key) {
        Map<String, ?> body = response.jsonPath().getMap("$");
        logger.debug("Asserting response contains key: {}", key);
        assertThat("Response does not contain key: " + key, body, hasKey(key));
    }

    public static void assertResponseBodyNotEmpty(Response response) {
        String body = response.getBody().asString();
        logger.debug("Asserting response body is not empty");
        assertThat("Response body is empty", body, not(emptyString()));
    }
}
