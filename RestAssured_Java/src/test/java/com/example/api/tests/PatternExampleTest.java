package com.example.api.tests;

import com.example.api.assertions.AssertionHelper;
import com.example.api.core.ApiCommand;
import com.example.api.core.BaseApiTest;
import com.example.api.core.ErrorHandler;
import com.example.api.core.RequestBuilder;
import com.example.api.core.ResponseDecorator;
import com.example.api.core.RetryStrategy;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class PatternExampleTest extends BaseApiTest {

    private RequestSpecification customSpec;

    @Override
    @BeforeClass(alwaysRun = true)
    public void setUpBase() {
        super.setUpBase();
        customSpec = new RequestBuilder()
                .baseUri(config.baseUrl())
                .basePath("/posts")
                .contentType(ContentType.JSON)
                .build();
    }

    @Test(description = "Example: Using Command Pattern for API calls",
            groups = "smoke")
    public void testUsingCommandPattern() {
        ApiCommand getCommand = new ApiCommand.GetCommand("/1", requestSpec);
        
        Response response = getCommand.execute();
        
        AssertionHelper.assertStatusCode(response, 200);
        AssertionHelper.assertJsonPath(response, "id", 1);
    }

    @Test(description = "Example: Using Decorator Pattern for response enhancement",
            groups = "smoke")
    public void testUsingDecoratorPattern() {
        Response response = given()
                .spec(requestSpec)
                .get("/posts/1");

        ResponseDecorator.decorate(response)
                .withLogging()
                .withTiming()
                .withErrorHandling();

        AssertionHelper.assertStatusCode(response, 200);
    }

    @Test(description = "Example: Using Strategy Pattern for retry logic",
            groups = "regression")
    public void testUsingStrategyPattern() {
        RetryStrategy retryStrategy = RetryStrategy.forServerErrors(2, 500);

        Response response = retryStrategy.executeWithRetry(() ->
                given()
                        .spec(requestSpec)
                        .get("/posts/1")
        );

        AssertionHelper.assertStatusCode(response, 200);
    }

    @Test(description = "Example: Using Builder Pattern for custom requests",
            groups = "regression")
    public void testUsingBuilderPattern() {
        RequestSpecification spec = new RequestBuilder()
                .baseUri(config.baseUrl())
                .basePath("/posts")
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .build();

        Response response = given()
                .spec(spec)
                .get("/1");

        AssertionHelper.assertStatusCode(response, 200);
        AssertionHelper.assertContentType(response, "application/json");
    }

    @Test(description = "Example: Using centralized assertion helpers",
            groups = "regression")
    public void testUsingAssertionHelpers() {
        Response response = given()
                .spec(requestSpec)
                .get("/posts/1");

        AssertionHelper.assertStatusCode(response, 200);
        AssertionHelper.assertSuccess(response);
        AssertionHelper.assertContentType(response, "application/json");
        AssertionHelper.assertJsonPath(response, "id", 1);
        AssertionHelper.assertJsonPathNotNull(response, "title");
        AssertionHelper.assertResponseTime(response, 5000);
        AssertionHelper.assertResponseBodyNotEmpty(response);
    }

    @Test(description = "Example: Using error handler for comprehensive error reporting",
            groups = "regression")
    public void testUsingErrorHandler() {
        try {
            Response response = given()
                    .spec(requestSpec)
                    .get("/posts/99999");

            if (response.getStatusCode() >= 400) {
                String errorReport = ErrorHandler.handleApiError(response, "Get post by ID");
                System.out.println("Error Report:\n" + errorReport);
            }

            boolean isRetryable = ErrorHandler.isRetryableError(response);
            System.out.println("Is retryable: " + isRetryable);

            String friendlyMessage = ErrorHandler.getErrorMessage(response);
            System.out.println("Error Message: " + friendlyMessage);

        } catch (Exception e) {
            String errorReport = ErrorHandler.handleException(e, "Get post by ID");
            System.out.println("Exception Report:\n" + errorReport);
        }
    }

    @Test(description = "Example: Combining multiple patterns",
            groups = "regression")
    public void testCombiningPatterns() {
        RequestSpecification spec = new RequestBuilder()
                .baseUri(config.baseUrl())
                .basePath("/posts")
                .contentType(ContentType.JSON)
                .build();

        ApiCommand command = new ApiCommand.GetCommand("/1", spec);

        RetryStrategy strategy = RetryStrategy.forHttpErrors(1, 100);
        Response response = strategy.executeWithRetry(command::execute);

        ResponseDecorator.decorate(response)
                .withLogging()
                .withTiming()
                .withValidation(r -> r.getStatusCode() == 200);

        AssertionHelper.assertStatusCode(response, 200);
        AssertionHelper.assertJsonPath(response, "id", 1);
    }
}
