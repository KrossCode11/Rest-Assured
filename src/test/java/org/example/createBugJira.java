package org.example;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.example.files.createBugPayload;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.*;

public class createBugJira {

    @Test
    public void createBug() {

        RestAssured.baseURI="https://akhilxaxa.atlassian.net";

       String response= given()
                .header("Content-Type","application/json")
                .header("Authorization","Basic YWtoaWx4YXhhQGdtYWlsLmNvbTpBVEFUVDN4RmZHRjBWY3hGbFVFZ2V6QVJPdzZpVmZIbUp6Rk44TlFIcEV2VkVZVVpiOVBsZ0JYcmdLcUNyanQ1ZG5QLVpoaFl5QlhxREotTWtnN1Z0bVgzSEJORlNnZlVMeEZDUUJkRVE5WDVjRGlvUmlDc3JIRDE5Q21ZRE9ITVRnRjFLbkR6WjllR2lSREpsZnhZakFTNDZaZGRCMG9nU0JIamE5cEJ2b1MyY291WFJtcnQ3Ujg9OTczOTJFOEY=")
                .header("Accept","application/json")
                .body(createBugPayload.createBug())
                .when()
                .post("/rest/api/3/issue")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .response()
                .asString();


        JsonPath js=new JsonPath(response);
        String id=js.getString("id");
        System.out.println("createdID: "+id);


      String response2=  given()
//                .header("Content-Type","*/*")
                .header("Authorization","Basic YWtoaWx4YXhhQGdtYWlsLmNvbTpBVEFUVDN4RmZHRjBWY3hGbFVFZ2V6QVJPdzZpVmZIbUp6Rk44TlFIcEV2VkVZVVpiOVBsZ0JYcmdLcUNyanQ1ZG5QLVpoaFl5QlhxREotTWtnN1Z0bVgzSEJORlNnZlVMeEZDUUJkRVE5WDVjRGlvUmlDc3JIRDE5Q21ZRE9ITVRnRjFLbkR6WjllR2lSREpsZnhZakFTNDZaZGRCMG9nU0JIamE5cEJ2b1MyY291WFJtcnQ3Ujg9OTczOTJFOEY=")
                .header("Accept","application/json")
                .header("X-Atlassian-Token","no-check")
                .multiPart("file",new File("/Users/a38748/Pictures/fallbackcase.png"))
                .when()
                .post("/rest/api/3/issue/"+id+"/attachments")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .response()
                .asString();

      JsonPath js2=new JsonPath(response2);
      String name=js2.getString("author.displayName");
      System.out.println("name: "+name);

    }
}
