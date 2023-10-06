package com.herokuapp.restfulbookers;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;


public class BaseTest {
   protected RequestSpecification spec;
   protected Booking booking;
    @BeforeSuite
    public void setUp() {
        spec = new RequestSpecBuilder()
                .setBaseUri("https://restful-booker.herokuapp.com")
                .build();
    }
    protected Response createBooking() {
        // create JSON body
        JSONObject body = new JSONObject();
        body.put("firstname", "Yuliia");
        body.put("lastname", "Terentieva");
        body.put("totalprice", 150);
        body.put("depositpaid", false);

        JSONObject bookingDates = new JSONObject();
        bookingDates.put("checkin", "2024-06-01");
        bookingDates.put("checkout", "2024-06-10");
        body.put("bookingdates", bookingDates);

        body.put("additionalneeds", "Coffee for the breakfast");

        //get response
        Response response = RestAssured.given(spec).contentType(ContentType.JSON).
                body(body.toString()).post("/booking");
        return response;
    }

}
