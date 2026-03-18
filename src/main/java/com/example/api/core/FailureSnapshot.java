package com.example.api.core;

public record FailureSnapshot(
        String requestMethod,
        String requestUri,
        String requestHeaders,
        String requestBody,
        Integer responseStatusCode,
        String responseStatusLine,
        String responseHeaders,
        String responseBody,
        Long responseTimeMs,
        String transportError,
        String capturedAt
) {
}
