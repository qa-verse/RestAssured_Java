package com.example.api.core;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class RequestBuilder {

    private static final Logger logger = LoggerFactory.getLogger(RequestBuilder.class);

    private String baseUri;
    private String basePath;
    private ContentType contentType;
    private final Map<String, String> headers;
    private final Map<String, String> queryParams;
    private final Map<String, String> pathParams;
    private Object body;

    public RequestBuilder() {
        this.headers = new HashMap<>();
        this.queryParams = new HashMap<>();
        this.pathParams = new HashMap<>();
    }

    public RequestBuilder baseUri(String baseUri) {
        this.baseUri = baseUri;
        logger.debug("Setting base URI: {}", baseUri);
        return this;
    }

    public RequestBuilder basePath(String basePath) {
        this.basePath = basePath;
        logger.debug("Setting base path: {}", basePath);
        return this;
    }

    public RequestBuilder contentType(ContentType contentType) {
        this.contentType = contentType;
        logger.debug("Setting content type: {}", contentType);
        return this;
    }

    public RequestBuilder header(String name, String value) {
        this.headers.put(name, value);
        logger.debug("Adding header: {}={}", name, value);
        return this;
    }

    public RequestBuilder headers(Map<String, String> headers) {
        this.headers.putAll(headers);
        logger.debug("Adding headers: {}", headers);
        return this;
    }

    public RequestBuilder queryParam(String name, String value) {
        this.queryParams.put(name, value);
        logger.debug("Adding query param: {}={}", name, value);
        return this;
    }

    public RequestBuilder queryParams(Map<String, String> params) {
        this.queryParams.putAll(params);
        logger.debug("Adding query params: {}", params);
        return this;
    }

    public RequestBuilder pathParam(String name, String value) {
        this.pathParams.put(name, value);
        logger.debug("Adding path param: {}={}", name, value);
        return this;
    }

    public RequestBuilder pathParams(Map<String, String> params) {
        this.pathParams.putAll(params);
        logger.debug("Adding path params: {}", params);
        return this;
    }

    public RequestBuilder body(Object body) {
        this.body = body;
        logger.debug("Setting request body");
        return this;
    }

    public RequestSpecification build() {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addFilter(new FailureCaptureFilter());

        if (baseUri != null) {
            builder.setBaseUri(baseUri);
        }

        if (basePath != null) {
            builder.setBasePath(basePath);
        }

        if (contentType != null) {
            builder.setContentType(contentType);
        }

        headers.forEach(builder::addHeader);
        queryParams.forEach(builder::addQueryParam);
        pathParams.forEach(builder::addPathParam);

        if (body != null) {
            builder.setBody(body);
        }

        logger.info("Building RequestSpecification with baseUri={}, basePath={}", baseUri, basePath);
        return builder.build();
    }
}
