package com.example.api.core;

import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

public class RetryStrategy {

    private static final Logger logger = LoggerFactory.getLogger(RetryStrategy.class);

    private final int maxRetries;
    private final long delayMillis;
    private final Predicate<Response> retryCondition;

    public RetryStrategy(int maxRetries, long delayMillis, Predicate<Response> retryCondition) {
        this.maxRetries = maxRetries;
        this.delayMillis = delayMillis;
        this.retryCondition = retryCondition;
    }

    public Response executeWithRetry(java.util.function.Supplier<Response> requestExecutor) {
        Response response = null;
        int attempt = 0;

        while (attempt <= maxRetries) {
            try {
                if (attempt > 0) {
                    logger.info("Retry attempt {} of {}", attempt, maxRetries);
                    Thread.sleep(delayMillis);
                }

                response = requestExecutor.get();

                if (!retryCondition.test(response)) {
                    logger.debug("Request succeeded on attempt {}", attempt + 1);
                    return response;
                }

                logger.warn("Request failed on attempt {}: status={}", attempt + 1, response.getStatusCode());
                attempt++;

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Retry interrupted", e);
                break;
            } catch (Exception e) {
                logger.error("Exception during retry attempt {}", attempt + 1, e);
                attempt++;
            }
        }

        logger.error("All retry attempts exhausted after {} attempts", maxRetries + 1);
        return response;
    }

    public static RetryStrategy forHttpErrors(int maxRetries, long delayMillis) {
        return new RetryStrategy(maxRetries, delayMillis, response -> {
            int status = response.getStatusCode();
            return status >= 400;
        });
    }

    public static RetryStrategy forServerErrors(int maxRetries, long delayMillis) {
        return new RetryStrategy(maxRetries, delayMillis, response -> {
            int status = response.getStatusCode();
            return status >= 500 && status < 600;
        });
    }

    public static RetryStrategy forTimeouts(int maxRetries, long delayMillis) {
        return new RetryStrategy(maxRetries, delayMillis, response -> {
            return response == null || response.getStatusCode() == 0;
        });
    }
}
