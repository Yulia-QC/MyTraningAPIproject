package com.herokuapp.restfulbookers;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;


public class DeleteBookingTests extends BaseTest {
    @Test
    public void deleteBookingTest() {
        //create new booking
        Response responseCreate = createBooking();
        responseCreate.print();
        //get new booking id
        int bookingid = responseCreate.jsonPath().getInt("bookingid");


        //delete booking with authorization (auth().preemptive().basic("username","password").)
        Response responseDelete = RestAssured.given(spec).auth().preemptive().basic("admin", "password123")
                .delete("/booking/" + bookingid);
        responseDelete.print();


        //verifications
        //verify response 201
        Assert.assertEquals(responseDelete.getStatusCode(), 201, "Status code expected to be 201 but it is not");

        Response responseGet = RestAssured.given(spec).get("/booking/" + bookingid);
        responseGet.print();

        Assert.assertEquals(responseGet.getBody().asString(), "Not Found", "Body should be 'Not Found', but it is not.");

    }

}
