package com.example.api.core;

import com.example.api.config.FrameworkConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class RequestSpecFactory {

    private static RequestSpecification cachedSpec;
    private static String cacheKey;

    private RequestSpecFactory() {
    }

    public static synchronized RequestSpecification build(FrameworkConfig config) {
        String newKey = config.baseUrl() + config.logLevel() + config.retryCount();
        if (cachedSpec == null || !newKey.equals(cacheKey)) {
            RequestSpecBuilder builder = new RequestSpecBuilder()
                    .setBaseUri(config.baseUrl())
                    .setContentType(ContentType.JSON)
                    .addFilter(new RetryOnFailureFilter(config.retryCount()))
                    .addFilter(new FailureCaptureFilter());

            if ("FULL".equalsIgnoreCase(config.logLevel())) {
                builder.addFilter(new RequestLoggingFilter())
                        .addFilter(new ResponseLoggingFilter());
            }

            cachedSpec = builder.build();
            cacheKey = newKey;
        }
        return cachedSpec;
    }
}
