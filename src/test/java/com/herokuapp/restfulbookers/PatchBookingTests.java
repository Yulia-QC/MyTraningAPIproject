package com.herokuapp.restfulbookers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;


public class PatchBookingTests extends BaseTest{
    Response response;
    @BeforeClass
    public void initializeBooking() {
        response= createBooking();
        response.print();
        Assert.assertEquals(response.getStatusCode(), 200, "Status code expected to be 200 but it is not");
    }
    @Test
    public void partialUpdateBookingTest() {

        //get new booking id
        int bookingid = response.jsonPath().getInt("bookingid");

        // create JSON body
        JSONObject body = new JSONObject();
        body.put("firstname", "Vlada");
//        body.put("lastname", "Terentiev");
//        body.put("totalprice", 100);
//        body.put("depositpaid", true);

        JSONObject bookingDates = new JSONObject();
        bookingDates.put("checkin", "2024-05-01");
        bookingDates.put("checkout", "2024-05-10");
        body.put("bookingdates", bookingDates);

//        body.put("additionalneeds", "More pillows");

        //update new booking with authorization (auth().preemptive().basic("username","password").)
        Response responseUpdate = RestAssured.given(spec).auth().preemptive().basic("admin","password123").contentType(ContentType.JSON).
                body(body.toString()).patch("/booking/" + bookingid);
        responseUpdate.print();


        //verifications
        //verify response 200
        Assert.assertEquals(responseUpdate.getStatusCode(), 200, "Status code expected to be 200 but it is not");

        //verify all fields
        SoftAssert softAssert = new SoftAssert();
        String actualFirstName = responseUpdate.jsonPath().getString("firstname");
        softAssert.assertEquals(actualFirstName, "Vlada", "firstname in response is not expected");

//        String actualLastName = responseUpdate.jsonPath().getString("lastname");
//        softAssert.assertEquals(actualLastName, "Terentiev", "lastname in response is not expected");

//        int totalPrice = responseUpdate.jsonPath().getInt("totalprice");
//        softAssert.assertEquals(totalPrice, 100, "Total price is not expected");
//
//        boolean depositIspaid = responseUpdate.jsonPath().getBoolean("depositpaid");
//        softAssert.assertTrue(depositIspaid, "Deposit paid should be true but it is not");

        String actualCheckin = responseUpdate.jsonPath().getString("bookingdates.checkin");
        softAssert.assertEquals(actualCheckin, "2024-05-01", "checking date is not expected");

        String actualCheckout = responseUpdate.jsonPath().getString("bookingdates.checkout");
        softAssert.assertEquals(actualCheckout, "2024-05-10", "checkout date is not expected");
//
//        String actualadditionalNeeds = responseUpdate.jsonPath().getString("additionalneeds");
//        softAssert.assertEquals(actualadditionalNeeds, "More pillows", "checkout date is not expected");

        softAssert.assertAll();

    }
//    @AfterClass(alwaysRun = true)
//    public void deleteInitializedBooking() {
//        if (response != null) {
//            int bookingid = response.jsonPath().getInt("bookingid");
//
//            //delete booking with authorization (auth().preemptive().basic("username","password").)
//            Response responseDelete = RestAssured.given(spec).auth().preemptive().basic("admin", "password123")
//                    .delete("/booking/" + bookingid);
//            responseDelete.print();
//        }
//    }

}
