# Naming Conventions

This document outlines the naming conventions used throughout the RestAssured Java testing framework to ensure consistency and maintainability.

## Package Naming

- **Format**: `com.example.api.<module>`
- **Examples**:
  - `com.example.api.config` - Configuration management
  - `com.example.api.core` - Core framework components
  - `com.example.api.clients` - API client classes
  - `com.example.api.utils` - Utility classes
  - `com.example.api.tests` - Test classes
  - `com.example.api.assertions` - Assertion helpers

## Class Naming

### Test Classes
- **Format**: `<Feature>Tests.java` or `<Feature>Test.java`
- **Suffix**: Must end with `Test` or `Tests`
- **Examples**:
  - `UsersTests.java` - Tests for user-related endpoints
  - `HealthCheckTest.java` - Health check tests
  - `AuthenticationTests.java` - Authentication tests

### Client Classes
- **Format**: `<Resource>Client.java`
- **Suffix**: Must end with `Client`
- **Examples**:
  - `UsersClient.java` - Client for user endpoints
  - `PostsClient.java` - Client for post endpoints
  - `CommentsClient.java` - Client for comment endpoints

### Utility Classes
- **Format**: `<Purpose>Loader.java`, `<Purpose>Helper.java`, or `<Purpose>Utils.java`
- **Examples**:
  - `SchemaLoader.java` - Loads JSON schemas
  - `TestDataLoader.java` - Loads test data
  - `AssertionHelper.java` - Assertion utilities

### Configuration Classes
- **Format**: `<Purpose>Config.java` or `<Purpose>Manager.java`
- **Examples**:
  - `FrameworkConfig.java` - Framework configuration
  - `ConfigManager.java` - Configuration manager

### Factory Classes
- **Format**: `<Resource>Factory.java`
- **Examples**:
  - `RequestSpecFactory.java` - Creates request specifications

## Method Naming

### Test Methods
- **Format**: `<action>Should<expectedResult>()` or `test<Feature><Action>()`
- **Examples**:
  - `getUserShouldMatchSchema()`
  - `createUserShouldReturn201()`
  - `healthEndpointShouldBeReachable()`

### Client Methods
- **Format**: `<httpMethod><Resource>()` or `<action><Resource>()`
- **Examples**:
  - `getUser(int userId)`
  - `createUser(Object payload)`
  - `updateUser(int userId, Object payload)`
  - `deleteUser(int userId)`

### Helper/Utility Methods
- **Format**: `<action><Resource>()` or `is<Condition>()` or `has<Property>()`
- **Examples**:
  - `loadData(String fileName)`
  - `jsonSchema(String schemaFile)`
  - `assertStatusCode(Response response, int expectedCode)`
  - `isSuccessful(Response response)`

## Variable Naming

### Instance Variables
- **Format**: camelCase
- **Examples**:
  - `requestSpec`
  - `usersClient`
  - `config`

### Constants
- **Format**: UPPER_SNAKE_CASE
- **Examples**:
  - `DEFAULT_ENV`
  - `CONFIG_DIR`
  - `MAX_RETRY_COUNT`

### Local Variables
- **Format**: camelCase
- **Examples**:
  - `response`
  - `userId`
  - `payload`

## File and Directory Naming

### Configuration Files
- **Format**: `<environment>.properties`
- **Examples**:
  - `local.properties`
  - `qa.properties`
  - `staging.properties`

### Test Data Files
- **Format**: `<resource>.json` or `<feature>.json`
- **Examples**:
  - `users.json`
  - `posts.json`
  - `test-data.json`

### Schema Files
- **Format**: `<resource>-schema.json`
- **Examples**:
  - `user-schema.json`
  - `post-schema.json`
  - `error-schema.json`

### Environment Files
- **Format**: `.env` or `.env.<environment>`
- **Examples**:
  - `.env`
  - `.env.example`
  - `.env.local`

## TestNG Annotations and Groups

### Test Groups
- **Format**: lowercase, descriptive
- **Examples**:
  - `smoke` - Quick smoke tests
  - `regression` - Full regression suite
  - `integration` - Integration tests
  - `api` - API-specific tests

### Test Descriptions
- **Format**: Clear, descriptive sentences
- **Examples**:
  - `"Validate GET /users/{id} returns schema-compliant payload"`
  - `"Verify user creation returns 201 status code"`

## API Endpoint Naming

### Base Paths
- **Format**: `/resource` (plural, lowercase)
- **Examples**:
  - `/users`
  - `/posts`
  - `/comments`

### Path Parameters
- **Format**: `{resourceId}` (camelCase)
- **Examples**:
  - `{userId}`
  - `{postId}`
  - `{commentId}`

## Best Practices

1. **Be Descriptive**: Names should clearly indicate purpose
2. **Consistency**: Follow the same pattern throughout the project
3. **Avoid Abbreviations**: Use full words unless widely understood
4. **Single Responsibility**: Each class/method should have one clear purpose
5. **Follow Java Conventions**: Adhere to standard Java naming conventions

