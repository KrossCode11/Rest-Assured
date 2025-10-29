package org.example;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.example.files.payload;
import org.testng.Assert;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class basics {

    public static void main(String[] args) {

        RestAssured.baseURI = "https://rahulshettyacademy.com";

       String response= given().log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(payload.AddPlace())
                .when()
                .post("maps/api/place/add/json")
                .then()
                .assertThat()
                .statusCode(200)
                .body("scope",equalTo("APP"))
                .header("server","Apache/2.4.52 (Ubuntu)")
                .extract()
                .asString();


       JsonPath js= new JsonPath(response);
       String placeId=js.getString("place_id");
        System.out.println(placeId);

        /*Update API*/
        given()
                .log().all().queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "  \"place_id\": \""+placeId+"\",\n" +
                        "  \"address\": \"70 Summer walk, USA\",\n" +
                        "  \"key\": \"qaclick123\"\n" +
                        "}")
                .when()
                .put("maps/api/place/update/json")
                .then()
                .assertThat()
                .log().all()
                .statusCode(200)
                .body("msg",equalTo("Address successfully updated"));


     String response2=   given()
                .when()
                .queryParam("key", "qaclick123")
                .queryParam("place_id", placeId)
                .get("maps/api/place/get/json")
                .then()
                .assertThat()
                .statusCode(200)
                .extract().asString();

        JsonPath js2=new JsonPath(response2);
        String address= js2.getString("address");
        System.out.println(address);


        Assert.assertEquals(address,"70 Summer walk, USA");

    }
}
