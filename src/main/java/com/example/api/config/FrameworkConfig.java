package com.example.api.config;

public record FrameworkConfig(String baseUrl,
                              String healthEndpoint,
                              int healthExpectedId,
                              int timeout,
                              int retryCount,
                              String logLevel) {

    public static FrameworkConfig fromProperties() {
        return new FrameworkConfig(
                ConfigManager.get("baseUrl"),
                ConfigManager.get("healthEndpoint"),
                Integer.parseInt(ConfigManager.get("health.expectedId")),
                Integer.parseInt(ConfigManager.get("timeout")),
                Integer.parseInt(ConfigManager.get("retry.count")),
                ConfigManager.get("log.level")
        );
    }
}
