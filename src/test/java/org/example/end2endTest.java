package org.example;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.pojo.E2EFlow.*;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class end2endTest {
    @Test
    public void end2endTest() {

//        RestAssured.baseURI="https://rahulshettyacademy.com/api/ecom";
        RequestSpecification req =new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com/api/ecom").setContentType(ContentType.JSON).build();


        LoginRequest lb =new LoginRequest();
        lb.setUserEmail("akhilxaxa@gmail.com");
        lb.setUserPassword("Akhil1234");

        RequestSpecification reqLogin= given()
                .spec(req)
                .body(lb);

      LoginResponse lr =
              reqLogin
                      .when()
               .post("/auth/login")
               .then()
               .assertThat()
               .statusCode(200)
               .extract()
               .as(LoginResponse.class);
        String token=lr.getToken();
        String userID= lr.getUserId();
        System.out.println("token: "+lr.getToken());


        /*Add product*/

        RequestSpecification baseReqAddProduct =new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com/api/ecom")
                .addHeader("Authorization",token)
                .build();

         RequestSpecification reqAddProduct=given()
                .spec(baseReqAddProduct)
                 .param("productName","Laptop")
                 .param("productAddedBy",userID)
                 .param("productPrice",11500)
                 .param("productDescription","Addias Originals")
                 .param("productCategory","fashion")
                 .param("productSubCategory","shirts")
                 .param("productFor","women")
                 .multiPart("productImage",new File("/Users/a38748/Pictures/fallbackcase.png"));


         AddProductResponse ap= reqAddProduct.when().post("/product/add-product")
                 .then()
                 .assertThat()
                 .statusCode(201)
                 .extract()
                 .response()
                 .as(AddProductResponse.class);

         String productId=ap.getProductId();
         System.out.println("productId: "+productId);


         /*createProduct*/

        RequestSpecification baseReqCreateProduct =new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com/api/ecom")
                .addHeader("Authorization",token)
                .setContentType(ContentType.JSON)
                .build();


        Orders cor =new Orders();
        OrderDetails cor2 =new OrderDetails();

        cor2.setCountry("India");
        cor2.setProductOrderedId(productId);

        List<OrderDetails> orderDetailsList=new ArrayList<OrderDetails>();
        orderDetailsList.add(cor2);
        cor.setOrders(orderDetailsList);

        CreateOrderResponse createOrderRes= given()
                .spec(baseReqCreateProduct)
                .body(cor)
                .log()
                .all()
                .when()
                .post("/order/create-order")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(201)
                .extract()
                .response()
                .as(CreateOrderResponse.class);

        String productOrderId= createOrderRes.getProductOrderId().get(0);
        String orderId=createOrderRes.getOrders().get(0);

        System.out.println( orderId);
        System.out.println( productOrderId);
        System.out.println( createOrderRes.getMessage());


            /*Delete Order*/


        RequestSpecification baseReqDeleteOrder =new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com/api/ecom")
                .addHeader("Authorization",token)
//                .setContentType(ContentType.JSON)
                .build();


        given().spec(baseReqDeleteOrder)
                .delete("/order/delete-order/"+orderId+"")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(200)
                .extract()
                .response()
                .asString();


            /*Delete Product*/

        RequestSpecification baseReqDeleteProduct =new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com/api/ecom")
                .addHeader("Authorization",token)
                .build();

        given().spec(baseReqDeleteProduct)
                .delete("/product/delete-product/"+productOrderId+"")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(200)
                .extract()
                .response()
                .asString();

    }
}
