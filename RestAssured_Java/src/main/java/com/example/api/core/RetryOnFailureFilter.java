package com.example.api.core;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class RetryOnFailureFilter implements Filter {

    private final int retries;

    public RetryOnFailureFilter(int retries) {
        this.retries = Math.max(retries, 0);
    }

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);
        int attempt = 0;
        while (!isSuccessful(response) && attempt < retries) {
            attempt++;
            response = ctx.next(requestSpec, responseSpec);
        }
        return response;
    }

    private boolean isSuccessful(Response response) {
        int status = response.statusCode();
        return status >= 200 && status < 300;
    }
}
