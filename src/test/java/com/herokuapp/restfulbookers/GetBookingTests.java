package com.herokuapp.restfulbookers;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class GetBookingTests extends BaseTest {
    Response response;
    @BeforeClass
    public void initializeBooking() {
        response= createBooking();
        response.print();
        Assert.assertEquals(response.getStatusCode(), 200, "Status code expected to be 200 but it is not");
    }

    @Test
    public void getBookingTest() {

        //set path parameter
        spec.pathParam("bookingid", response.jsonPath().getInt("bookingid"));

        //Get response with bookingId
        Response response = RestAssured.given(spec).get("/booking/{bookingid}");
        response.print();

        //verify response 200
        Assert.assertEquals(response.getStatusCode(), 200, "Status code expected to be 200 but it is not");

        //verify all fields
        SoftAssert softAssert = new SoftAssert();
        String actualFirstName = response.jsonPath().getString("firstname");
        softAssert.assertEquals(actualFirstName, "Yuliia", "firstname in response is not expected");

        String actualLastName = response.jsonPath().getString("lastname");
        softAssert.assertEquals(actualLastName, "Terentieva", "lastname in response is not expected");

        int totalPrice = response.jsonPath().getInt("totalprice");
        softAssert.assertEquals(totalPrice, 150, "Total price is not expected");

        boolean depositIspaid = response.jsonPath().getBoolean("depositpaid");
        softAssert.assertFalse(depositIspaid, "Deposit paid should be true but it is not");

        String actualCheckin = response.jsonPath().getString("bookingdates.checkin");
        softAssert.assertEquals(actualCheckin, "2024-06-01", "checking date is not expected");

        String actualCheckout = response.jsonPath().getString("bookingdates.checkout");
        softAssert.assertEquals(actualCheckout, "2024-06-10", "checkout date is not expected");

        String actualadditionalNeeds = response.jsonPath().getString("additionalneeds");
        softAssert.assertEquals(actualadditionalNeeds, "Coffee for the breakfast", "checkout date is not expected");

        softAssert.assertAll();

        Bookingdates bookingdates = new Bookingdates(response.jsonPath().getString("bookingdates.checkin"),
                response.jsonPath().getString("bookingdates.checkout"));
        booking = new Booking(response.jsonPath().getString("firstname"),
                 response.jsonPath().getString("lastname"),
                response.jsonPath().getInt("totalprice"),
                response.jsonPath().getBoolean("depositpaid"),
                bookingdates,
                response.jsonPath().getString("additionalneeds"));
    }

    @Test
    public void getBookingXMLTest() {   //for SOAP API

        //set path parameter
        spec.pathParam("bookingid", response.jsonPath().getInt("bookingid"));

        //Get response with booking
        Header xml = new Header("Accept", "application/xml");
        spec.header(xml);
        Response response = RestAssured.given(spec).get("/booking/{bookingid}");
        response.print();

        //verify response 200
        Assert.assertEquals(response.getStatusCode(), 200, "Status code expected to be 200 but it is not");

        //verify all fields
        SoftAssert softAssert = new SoftAssert();
        String actualFirstName = response.xmlPath().getString("booking.firstname");
        softAssert.assertEquals(actualFirstName, "Yuliia", "firstname in response is not expected");

        String actualLastName = response.xmlPath().getString("booking.lastname");
        softAssert.assertEquals(actualLastName, "Terentieva", "lastname in response is not expected");

        int totalPrice = response.xmlPath().getInt("booking.totalprice");
        softAssert.assertEquals(totalPrice, 150, "Total price is not expected");

        boolean depositIspaid = response.xmlPath().getBoolean("booking.depositpaid");
        softAssert.assertFalse(depositIspaid, "Deposit paid should be true but it is not");

        String actualCheckin = response.xmlPath().getString("booking.bookingdates.checkin");
        softAssert.assertEquals(actualCheckin, "2024-06-01", "checking date is not expected");

        String actualCheckout = response.xmlPath().getString("booking.bookingdates.checkout");
        softAssert.assertEquals(actualCheckout, "2024-06-10", "checkout date is not expected");

        String actualadditionalNeeds = response.xmlPath().getString("booking.additionalneeds");
        softAssert.assertEquals(actualadditionalNeeds, "Coffee for the breakfast", "checkout date is not expected");

        softAssert.assertAll();
    }

}
