package com.example.api.core;

import com.example.api.config.FrameworkConfig;
import com.example.api.config.ConfigManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public abstract class BaseApiTest {

    protected FrameworkConfig config;
    protected RequestSpecification requestSpec;
    protected ObjectMapper jsonMapper;
    protected ObjectMapper yamlMapper;

    @BeforeClass(alwaysRun = true)
    public void setUpBase() {
        config = FrameworkConfig.fromProperties();
        requestSpec = RequestSpecFactory.build(config);
        jsonMapper = new ObjectMapper();
        yamlMapper = new ObjectMapper(new YAMLFactory());
    }

    protected String getEnv() {
        return System.getProperty("env", ConfigManager.getProperties().getProperty("env", "local"));
    }
}
