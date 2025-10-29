package org.example;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.example.files.payload;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class dynamicJson {

    @Test(dataProvider="BooksData")
    public static void bookJourney(String isbn,String aisle) {

        RestAssured.baseURI="http://216.10.245.166";
        String response=given()
                .header("Content-Type","application/json")
                .body(payload.AddBooks(isbn,aisle))
                .when()
                .post("/Library/Addbook.php")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response()
                .asString();


        JsonPath js=new JsonPath(response);
        String id=js.getString("ID");
        System.out.println("createdID: "+id);


        /*Delete book*/
        String deleteResponse=given()
                .body(payload.DeleteBooks(id))
                .when()
                .delete("/Library/DeleteBook.php")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .asString();

        JsonPath js2=new JsonPath(deleteResponse);
        String message=js2.get("msg");

        Assert.assertEquals(message,"book is successfully deleted");

    }

    @DataProvider(name="BooksData")
    public Object[][] getData(){
        return new Object[][] {
            {"hjse","567"},
            {"lkje","232"},
            {"xdfd","999"}
        };
    }
}
