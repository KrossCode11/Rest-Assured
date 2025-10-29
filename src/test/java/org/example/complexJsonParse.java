package org.example;

import io.restassured.path.json.JsonPath;
import org.example.files.payload;
import org.testng.Assert;

public class complexJsonParse {
    public static void main(String[] args) {

        JsonPath js= new JsonPath(payload.CoursePrice());
        int courseCount=js.getInt("courses.size()");
        System.out.println("courseCount: "+courseCount);


        /*Purchase amount*/
        int purchaseAmount=js.getInt("dashboard.purchaseAmount");
        System.out.println("purchaseAmount: "+purchaseAmount);

        /*Print Title of the first course*/

        String title=js.getString("courses[0].title");
        System.out.println("title: "+title);


        /*Print All course titles and their respective Prices*/

        for(int i=0;i<courseCount;i++){
            String courseTitle=js.getString("courses["+i+"].title");
            int price=js.getInt("courses["+i+"].price");
            System.out.println("courseTitle: "+courseTitle);
            System.out.println("price: "+price);
        }

        /*Print no of copies sold by RPA Course*/

        for(int i=0;i<courseCount;i++) {
            String courseTitle = js.getString("courses[" + i + "].title");
            if (courseTitle.equals("RPA")) {
                int copies = js.getInt("courses[" + i + "].copies");
                System.out.println("copies: " + copies);
                break;
            }
        }

        int count=0;
            for(int i=0;i<courseCount;i++){
                int price=js.getInt("courses["+i+"].price");
                int copies=js.getInt("courses["+i+"].copies");
                count=count+(price*copies);

            }
        Assert.assertEquals(count,purchaseAmount);



    }
}
