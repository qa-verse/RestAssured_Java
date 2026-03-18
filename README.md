REST ASSURED JAVA TESTING FRAMEWORK
===================================

A modular and scalable API automation framework built using **Java**, **RestAssured**, **TestNG**, and **Maven**.  
Designed for clean structure, maintainability, and real-world API automation needs.


OVERVIEW
--------
This framework provides a complete foundation for API test automation with multi-environment configuration, reusable request builders, shared assertions, retry logic, schema validation, reporting, and CI/CD integration.

It follows clean coding principles and uses industry design patterns to keep the project maintainable and scalable.


FEATURES
--------

Core Capabilities:
- Multi-environment support (local, QA, staging, production)
- `.env` and property-file merging
- Design patterns (Builder, Strategy, Factory, Command, Singleton, Decorator)
- Centralized assertion helpers
- Structured logging through Logback
- Retry mechanism for unstable endpoints
- JSON schema validation
- Allure test reporting
- GitHub Actions CI workflow
- External test data stored in JSON format


PREREQUISITES
-------------

Required Tools:
- Java 17+
- Maven 3.9+
- Git
- IntelliJ IDEA / Eclipse / VS Code

Verify Installation:
```bash
java -version
mvn -version
```


INSTALLATION
------------

Clone Repository:
```bash
git clone <repository-url>
cd RestAssured_Java
```


ENVIRONMENT CONFIGURATION
-------------------------

Option A: Property Files  
Located inside `config/`:
- local.properties
- qa.properties
- staging.properties

Option B: .env File:
```bash
cp .env.example .env
```

Sample .env:
```env
ENV=local
BASE_URL=https://jsonplaceholder.typicode.com
TIMEOUT=5000
RETRY_COUNT=1
LOG_LEVEL=BASIC
```

Configuration Priority (High → Low):
1. System properties  
2. Environment variables  
3. `.env` file  
4. `config/<env>.properties`  
5. `config/local.properties`  


PROJECT STRUCTURE
-----------------

```
RestAssured_Java/
├── config/
├── docs/
├── src/
│   ├── main/java/          # Core framework
│   ├── test/java/          # Test classes
├── .github/workflows/      # CI pipeline
├── pom.xml
└── README.md
```

Core Modules:
- clients/ – API client definitions
- core/ – Spec factory, request builder, retry logic, error handling
- assertions/ – Reusable assertion methods
- utils/ – Test data and schema loaders
- config/ – Environment configuration loader


RUNNING TESTS
-------------

Default run:
```bash
mvn clean test
```

Run on specific environment:
```bash
mvn clean test -Denv=qa
mvn clean test -Denv=staging
```

Run specific groups:
```bash
mvn test -Dgroups=smoke
mvn test -Dgroups=regression
```

Run a specific test class:
```bash
mvn test -Dtest=UsersTests
```


DESIGN PATTERNS
---------------

Patterns implemented in the framework:
- Singleton – ConfigManager, RequestSpecFactory
- Factory – RequestSpecFactory
- Builder – RequestBuilder
- Strategy – RetryStrategy
- Command – ApiCommand
- Decorator – ResponseDecorator


WRITING TESTS
-------------

Basic Example:
```java
@Test
public void testEndpoint() {
    given()
        .spec(requestSpec)
        .get("/endpoint")
        .then()
        .statusCode(200);
}
```

Using Assertion Helpers:
```java
Response response = usersClient.getUser(1);
AssertionHelper.assertStatusCode(response, 200);
```

Using Clients:
```java
@BeforeClass
public void setUp() {
    usersClient = new UsersClient(requestSpec);
}
```


LOGGING
-------

Logging is configured with Logback:

- Console Output  
- Rolling File Logs  
- Error-only Logs  

Log files rotate daily and include timestamps.


REPORTING
---------

Allure Report:
```bash
mvn clean test
mvn allure:serve
```

Surefire Reports:
```
target/surefire-reports/
```

JaCoCo Coverage:
```bash
mvn clean test jacoco:report
```

Coverage Output:
```
target/site/jacoco/
```


CI/CD INTEGRATION
-----------------

GitHub Actions Workflow Includes:
- Multi-environment matrix execution
- Checkstyle + SpotBugs
- Allure reports
- JaCoCo coverage
- Artifact uploads

The pipeline works out of the box.


ERROR HANDLING
--------------

Includes:
- Retry logic on failure
- Structured error messages
- Exception handling utilities
- Centralized error processing
- Automatic failure artifacts (`target/failure-artifacts/`) with traceback and request/response snapshot


NAMING CONVENTIONS
------------------

- Test classes → `*Tests.java`
- API clients → `*Client.java`
- Helpers/Utils → `*Helper.java`, `*Utils.java`
- Constants → `UPPER_SNAKE_CASE`


TROUBLESHOOTING
---------------

Common Issues:

**Config file missing**  
Ensure `config/<env>.properties` exists.

**Allure not generating**  
Run `mvn clean test` before `allure:serve`.

**Logs missing**  
Verify `logback.xml` and ensure a `logs/` directory exists.

**Dependencies not downloading**  
Check Maven configuration and network access.


LICENSE
-------

This project is intended for educational and API automation development purposes.
