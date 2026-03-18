package com.example.api.core;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class FailureCaptureFilter implements Filter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {
        String requestBody = "";
        if (requestSpec.getBody() != null) {
            requestBody = String.valueOf(requestSpec.getBody());
        }

        try {
            Response response = ctx.next(requestSpec, responseSpec);
            FailureCaptureContext.setSnapshot(new FailureSnapshot(
                    safeValue(requestSpec.getMethod()),
                    safeValue(requestSpec.getURI()),
                    safeValue(requestSpec.getHeaders()),
                    requestBody,
                    response.getStatusCode(),
                    safeValue(response.getStatusLine()),
                    safeValue(response.getHeaders()),
                    safeValue(response.getBody().asString()),
                    response.getTime(),
                    null,
                    LocalDateTime.now().format(FORMATTER)
            ));
            return response;
        } catch (RuntimeException exception) {
            FailureCaptureContext.setSnapshot(new FailureSnapshot(
                    safeValue(requestSpec.getMethod()),
                    safeValue(requestSpec.getURI()),
                    safeValue(requestSpec.getHeaders()),
                    requestBody,
                    null,
                    "",
                    "",
                    "",
                    null,
                    exception.getClass().getName() + ": " + safeValue(exception.getMessage()),
                    LocalDateTime.now().format(FORMATTER)
            ));
            throw exception;
        }
    }

    private String safeValue(Object value) {
        return value == null ? "" : String.valueOf(value);
    }
}
