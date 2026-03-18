package com.example.api.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public final class TestDataLoader {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private TestDataLoader() {
    }

    public static Map<String, Object> loadData(String fileName) {
        try (InputStream stream = TestDataLoader.class.getClassLoader().getResourceAsStream("data/" + fileName)) {
            if (stream == null) {
                throw new IllegalArgumentException("Test data not found: " + fileName);
            }
            return MAPPER.readValue(stream, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read data file" + fileName, e);
        }
    }
}
