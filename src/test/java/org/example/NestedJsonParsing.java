package org.example;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.codehaus.groovy.runtime.powerassert.SourceText;
import org.example.pojo.Api;
import org.example.pojo.GetCourses;
import org.example.pojo.WebAutomation;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class NestedJsonParsing {
    @Test
    public void getResponseFromComplexJson() {
        String webAutomationExpected[]={"Selenium Webdriver Java","Cypress","Protractor"};

        RestAssured.baseURI = "https://rahulshettyacademy.com/oauthapi";

        String authResponse = given()
                .formParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .formParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .formParam("grant_type", "client_credentials")
                .formParam("scope", "trust")
                .when()
                .post("/oauth2/resourceOwner/token")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response()
                .asString();

        JsonPath js1 = new JsonPath(authResponse);
        String accessToken = js1.getString("access_token");
        System.out.println("accessToken: " + accessToken);

        GetCourses response = given()
                .queryParam("access_token", accessToken)
                .when()
                .get("/getCourseDetails")
                .then()
                .assertThat()
                .statusCode(401)
                .extract()
                .as(GetCourses.class);

//        System.out.println(response.getInstructor());
//        System.out.println(response.getCourses().getWebAutomation().get(0).getCourseTitle());

//        int webAutomationCount = response.getCourses().getWebAutomation().size();
//        System.out.println("webAutomationCount: " + webAutomationCount);
        List<WebAutomation>webAutomationList=response.getCourses().getWebAutomation();

        for(int i=0;i<webAutomationList.size();i++){
//            System.out.println(webAutomationList.get(i).getCourseTitle());
            Assert.assertEquals(webAutomationList.get(i).getCourseTitle(),webAutomationExpected[i]);
        }


         List<Api> api=response.getCourses().getApi();


        for (int i = 0; i < api.size(); i++) {
            if (api.get(i).getCourseTitle().equalsIgnoreCase("Rest Assured Automation using Java")) {
            System.out.println(api.get(i).getCourseTitle());
                System.out.println(api.get(i).getPrice());
            }

        }
    }

}
