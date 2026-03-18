package com.example.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public final class EnvFileLoader {

    private static final Logger logger = LoggerFactory.getLogger(EnvFileLoader.class);
    private static final String ENV_FILE = ".env";

    private EnvFileLoader() {
    }

    public static Map<String, String> loadEnvFile() {
        Map<String, String> envVars = new HashMap<>();
        Path envPath = Paths.get(ENV_FILE);

        if (!envPath.toFile().exists()) {
            logger.debug(".env file not found at: {}", envPath.toAbsolutePath());
            return envVars;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(envPath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                int equalsIndex = line.indexOf('=');
                if (equalsIndex > 0) {
                    String key = line.substring(0, equalsIndex).trim();
                    String value = line.substring(equalsIndex + 1).trim();
                    
                    if ((value.startsWith("\"") && value.endsWith("\"")) ||
                        (value.startsWith("'") && value.endsWith("'"))) {
                        value = value.substring(1, value.length() - 1);
                    }
                    
                    envVars.put(key, value);
                    logger.debug("Loaded env var: {}={}", key, value.isEmpty() ? "<empty>" : "***");
                }
            }
            logger.info("Loaded {} environment variables from .env file", envVars.size());
        } catch (IOException e) {
            logger.warn("Failed to load .env file: {}", e.getMessage());
        }

        return envVars;
    }

    public static void setSystemProperties(Map<String, String> envVars) {
        envVars.forEach((key, value) -> {
            if (System.getProperty(key) == null) {
                System.setProperty(key, value);
                logger.debug("Set system property from .env: {}", key);
            }
        });
    }
}
