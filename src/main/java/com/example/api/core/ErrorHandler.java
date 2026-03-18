package com.example.api.core;

import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private ErrorHandler() {
    }

    public static String handleApiError(Response response, String context) {
        int statusCode = response.getStatusCode();
        String body = response.getBody().asString();
        String headers = response.getHeaders().toString();

        String errorMessage = String.format(
                "API Error in %s%n" +
                "Status Code: %d%n" +
                "Response Body: %s%n" +
                "Response Headers: %s%n" +
                "Timestamp: %s",
                context, statusCode, body, headers, LocalDateTime.now().format(FORMATTER)
        );

        logger.error(errorMessage);

        if (statusCode >= 500) {
            logger.error("Server error detected - Status: {}", statusCode);
        } else if (statusCode >= 400) {
            logger.warn("Client error detected - Status: {}", statusCode);
        }

        return errorMessage;
    }

    public static String handleException(Exception exception, String context) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String stackTrace = sw.toString();

        String errorMessage = String.format(
                "Exception in %s%n" +
                "Exception Type: %s%n" +
                "Message: %s%n" +
                "Stack Trace:%n%s" +
                "Timestamp: %s",
                context,
                exception.getClass().getName(),
                exception.getMessage(),
                stackTrace,
                LocalDateTime.now().format(FORMATTER)
        );

        logger.error(errorMessage, exception);
        return errorMessage;
    }

    public static String createErrorReport(Response response, Exception exception, String context) {
        StringBuilder report = new StringBuilder();
        report.append("=".repeat(80)).append("\n");
        report.append("ERROR REPORT\n");
        report.append("=".repeat(80)).append("\n");
        report.append("Context: ").append(context).append("\n");
        report.append("Timestamp: ").append(LocalDateTime.now().format(FORMATTER)).append("\n\n");

        if (response != null) {
            report.append("Response Details:\n");
            report.append("  Status Code: ").append(response.getStatusCode()).append("\n");
            report.append("  Status Line: ").append(response.getStatusLine()).append("\n");
            report.append("  Response Time: ").append(response.getTime()).append("ms\n");
            report.append("  Content Type: ").append(response.getContentType()).append("\n");
            report.append("  Response Body: ").append(response.getBody().asString()).append("\n");
            report.append("  Headers: ").append(response.getHeaders()).append("\n\n");
        }

        if (exception != null) {
            report.append("Exception Details:\n");
            report.append("  Type: ").append(exception.getClass().getName()).append("\n");
            report.append("  Message: ").append(exception.getMessage()).append("\n");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            exception.printStackTrace(pw);
            report.append("  Stack Trace:\n").append(sw.toString()).append("\n");
        }

        report.append("=".repeat(80)).append("\n");
        return report.toString();
    }

    public static boolean isRetryableError(Response response) {
        if (response == null) {
            return true;
        }

        int statusCode = response.getStatusCode();
        return statusCode >= 500 || statusCode == 429 || statusCode == 408;
    }

    public static String getErrorMessage(Response response) {
        if (response == null) {
            return "No response received from server";
        }

        int statusCode = response.getStatusCode();
        String body = response.getBody().asString();

        switch (statusCode) {
            case 400:
                return "Bad Request: " + body;
            case 401:
                return "Unauthorized: Authentication required";
            case 403:
                return "Forbidden: Access denied";
            case 404:
                return "Not Found: Resource does not exist";
            case 408:
                return "Request Timeout: Server did not respond in time";
            case 429:
                return "Too Many Requests: Rate limit exceeded";
            case 500:
                return "Internal Server Error: " + body;
            case 502:
                return "Bad Gateway: Invalid response from upstream server";
            case 503:
                return "Service Unavailable: Server temporarily unavailable";
            case 504:
                return "Gateway Timeout: Upstream server did not respond";
            default:
                return String.format("HTTP Error %d: %s", statusCode, body);
        }
    }
}
