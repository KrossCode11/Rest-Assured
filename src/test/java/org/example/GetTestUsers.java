package org.example;

import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class GetTestUsers {

    @Test
    public void test1() {

        RestAssured.baseURI = "https://reqres.in";

        // GET request
        Response response = RestAssured
                .given()
                .when()
                .get("/api/users?page=2")
                .then()
                .statusCode(200)
                .extract()
                .response();

        // Assertions
        String firstUser = response.jsonPath().getString("data[0].first_name");
        System.out.println("First User: " + firstUser);

        Assert.assertNotNull(firstUser);
    }
}
