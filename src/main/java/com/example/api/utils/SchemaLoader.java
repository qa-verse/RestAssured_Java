package com.example.api.utils;

import io.restassured.module.jsv.JsonSchemaValidator;
import org.hamcrest.Matcher;

import java.io.InputStream;

public final class SchemaLoader {

    private SchemaLoader() {
    }

    public static Matcher<?> jsonSchema(String schemaFile) {
        InputStream schemaStream = SchemaLoader.class
                .getClassLoader()
                .getResourceAsStream("schemas/" + schemaFile);
        if (schemaStream == null) {
            throw new IllegalArgumentException("Schema not found: " + schemaFile);
        }
        return JsonSchemaValidator.matchesJsonSchema(schemaStream);
    }
}
