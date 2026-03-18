package com.example.api.tests;

import com.example.api.clients.UsersClient;
import com.example.api.core.BaseApiTest;
import com.example.api.utils.SchemaLoader;
import com.example.api.utils.TestDataLoader;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UsersTests extends BaseApiTest {

    private UsersClient usersClient;

    @Override
    @BeforeClass(alwaysRun = true)
    public void setUpBase() {
        super.setUpBase();
        usersClient = new UsersClient(requestSpec);
    }

    @Test(description = "Validate GET /users/{id} returns schema-compliant payload",
            groups = {"smoke", "regression"})
    public void getUserShouldMatchSchema() {
        usersClient.getUser(1)
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", notNullValue())
                .body(SchemaLoader.jsonSchema("user-schema.json"));
    }

    @DataProvider
    public Object[][] userPayloads() {
        Map<String, Object> data = TestDataLoader.loadData("users.json");
        return new Object[][]{
                {data.get("validUser")}
        };
    }

    @Test(dataProvider = "userPayloads", groups = "regression")
    public void createUserShouldReturn201(Object payload) {
        Response response = usersClient.createUser(payload);
        response.then()
                .statusCode(201)
                .body("name", equalTo(((Map<?, ?>) payload).get("name")));
    }
}
