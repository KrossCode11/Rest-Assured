package org.example;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class clientOAuthTest {
    @Test
    public void getCoursesUsingOath(){
        RestAssured.baseURI="https://rahulshettyacademy.com/oauthapi";

       String authResponse= given()
                .formParam("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .formParam("client_secret","erZOWM9g3UtwNRj340YYaK_W")
                .formParam("grant_type","client_credentials")
                .formParam("scope","trust")
                .when()
                .post("/oauth2/resourceOwner/token")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response()
                .asString();

        JsonPath js1=new JsonPath(authResponse);
        String accessToken=js1.getString("access_token");
        System.out.println("accessToken: "+accessToken);

        String response=given()
                .queryParam("access_token",accessToken)
                .when()
                .get("/getCourseDetails")
                .then()
                .assertThat()
                .statusCode(401)
                .extract()
                .asString();

        JsonPath js2=new JsonPath(response);
        String message=js2.get("linkedIn");
        System.out.println("linkedInURL: "+message);
    }

}
