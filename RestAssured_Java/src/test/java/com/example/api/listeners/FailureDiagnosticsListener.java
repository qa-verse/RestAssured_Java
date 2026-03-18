package com.example.api.listeners;

import com.example.api.core.FailureCaptureContext;
import com.example.api.core.FailureSnapshot;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class FailureDiagnosticsListener implements ITestListener {

    private static final Logger logger = LoggerFactory.getLogger(FailureDiagnosticsListener.class);
    private static final Path ARTIFACTS_DIR = Paths.get("target", "failure-artifacts");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");
    private static final int MAX_FIELD_LENGTH = 20000;

    @Override
    public void onTestStart(ITestResult result) {
        FailureCaptureContext.clear();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        FailureCaptureContext.clear();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        FailureSnapshot snapshot = FailureCaptureContext.getSnapshot();
        String traceback = buildTraceback(result.getThrowable());
        String snapshotReport = buildSnapshotReport(snapshot);

        writeArtifacts(result, traceback, snapshotReport);
        addAttachment("Failure Traceback", traceback);
        addAttachment("Failure Request Response Snapshot", snapshotReport);

        FailureCaptureContext.clear();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        FailureCaptureContext.clear();
    }

    private void writeArtifacts(ITestResult result, String traceback, String snapshotReport) {
        Path outputDir = artifactDirectoryFor(result);
        try {
            Files.createDirectories(outputDir);
            Files.writeString(outputDir.resolve("traceback.txt"), traceback, StandardCharsets.UTF_8);
            Files.writeString(
                    outputDir.resolve("request-response-snapshot.txt"),
                    snapshotReport,
                    StandardCharsets.UTF_8
            );
            logger.error("Failure artifacts saved at {}", outputDir.toAbsolutePath());
        } catch (IOException exception) {
            logger.error("Failed to persist failure artifacts for {}", result.getName(), exception);
        }
    }

    private Path artifactDirectoryFor(ITestResult result) {
        String qualifiedTestName = result.getTestClass().getName() + "." + result.getMethod().getMethodName();
        String safeName = qualifiedTestName.replaceAll("[^a-zA-Z0-9._-]", "_");
        String timestamp = LocalDateTime.now().format(FORMATTER);
        return ARTIFACTS_DIR.resolve(safeName + "-" + timestamp);
    }

    private String buildTraceback(Throwable throwable) {
        if (throwable == null) {
            return "No throwable is available for this failure.";
        }

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    private String buildSnapshotReport(FailureSnapshot snapshot) {
        if (snapshot == null) {
            return "No request/response snapshot was captured for this test.";
        }

        StringBuilder report = new StringBuilder();
        report.append("Captured At: ").append(snapshot.capturedAt()).append("\n");
        report.append("Request Method: ").append(snapshot.requestMethod()).append("\n");
        report.append("Request URI: ").append(snapshot.requestUri()).append("\n");
        report.append("Request Headers: ").append(snapshot.requestHeaders()).append("\n");
        report.append("Request Body:\n").append(trim(snapshot.requestBody())).append("\n\n");

        if (snapshot.transportError() != null && !snapshot.transportError().isBlank()) {
            report.append("Transport Error: ").append(snapshot.transportError()).append("\n");
            return report.toString();
        }

        report.append("Response Status Code: ").append(snapshot.responseStatusCode()).append("\n");
        report.append("Response Status Line: ").append(snapshot.responseStatusLine()).append("\n");
        report.append("Response Time (ms): ").append(snapshot.responseTimeMs()).append("\n");
        report.append("Response Headers: ").append(snapshot.responseHeaders()).append("\n");
        report.append("Response Body:\n").append(trim(snapshot.responseBody())).append("\n");
        return report.toString();
    }

    private String trim(String value) {
        if (value == null) {
            return "";
        }
        if (value.length() <= MAX_FIELD_LENGTH) {
            return value;
        }
        return value.substring(0, MAX_FIELD_LENGTH) + "\n...[truncated]";
    }

    private void addAttachment(String name, String content) {
        if (content == null || content.isBlank()) {
            return;
        }
        Allure.addAttachment(name, "text/plain", content, ".txt");
    }
}
