package com.example.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public final class ConfigManager {

    private static final String DEFAULT_ENV = "local";
    private static final String CONFIG_DIR = "config";
    private static final Properties PROPERTIES = new Properties();
    private static volatile boolean initialized;

    private ConfigManager() {
    }

    public static Properties getProperties() {
        if (!initialized) {
            synchronized (ConfigManager.class) {
                if (!initialized) {
                    loadProperties();
                    initialized = true;
                }
            }
        }
        return PROPERTIES;
    }

    public static String get(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null) {
            return systemValue;
        }
        return getProperties().getProperty(key);
    }

    private static void loadProperties() {
        Map<String, String> envVars = EnvFileLoader.loadEnvFile();
        EnvFileLoader.setSystemProperties(envVars);
        
        String env = System.getProperty("env", System.getenv().getOrDefault("ENV", DEFAULT_ENV));
        Path defaultPath = Paths.get(CONFIG_DIR, DEFAULT_ENV + ".properties");
        Path envPath = Paths.get(CONFIG_DIR, env + ".properties");

        try (InputStream defaultStream = Files.newInputStream(defaultPath)) {
            PROPERTIES.load(defaultStream);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load default config from " + defaultPath, e);
        }

        if (!Objects.equals(env, DEFAULT_ENV)) {
            if (Files.notExists(envPath)) {
                throw new IllegalStateException("Config file not found for env: " + env);
            }
            try (InputStream envStream = Files.newInputStream(envPath)) {
                Properties envProps = new Properties();
                envProps.load(envStream);
                PROPERTIES.putAll(envProps);
            } catch (IOException e) {
                throw new IllegalStateException("Unable to load config for env: " + env, e);
            }
        }
        PROPERTIES.setProperty("env", env);
    }
}
