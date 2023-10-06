package com.herokuapp.restfulbookers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;


public class CreateBookingTests extends BaseTest {
    Response response;
    @BeforeClass
    public void initializeBooking() {
        response = createBooking();
        response.print();
        Assert.assertEquals(response.getStatusCode(), 200, "Status code expected to be 200 but it is not");
    }

    @Test
    public void createBookingTest() {

        //verify all fields
        SoftAssert softAssert = new SoftAssert();
        String actualFirstName = response.jsonPath().getString("booking.firstname");
        softAssert.assertEquals(actualFirstName, "Yuliia", "firstname in response is not expected");

        String actualLastName = response.jsonPath().getString("booking.lastname");
        softAssert.assertEquals(actualLastName, "Terentieva", "lastname in response is not expected");

        int totalPrice = response.jsonPath().getInt("booking.totalprice");
        softAssert.assertEquals(totalPrice, 150, "Total price is not expected");

        boolean depositIspaid = response.jsonPath().getBoolean("booking.depositpaid");
        softAssert.assertFalse(depositIspaid, "Deposit paid should be true but it is not");

        String actualCheckin = response.jsonPath().getString("booking.bookingdates.checkin");
        softAssert.assertEquals(actualCheckin, "2024-06-01", "checking date is not expected");

        String actualCheckout = response.jsonPath().getString("booking.bookingdates.checkout");
        softAssert.assertEquals(actualCheckout, "2024-06-10", "checkout date is not expected");

        String actualadditionalNeeds = response.jsonPath().getString("booking.additionalneeds");
        softAssert.assertEquals(actualadditionalNeeds, "Coffee for the breakfast", "checkout date is not expected");

        softAssert.assertAll();

    }

    @Test
    public void createBookingWithPOJOTest() {
        // create body using POJOs
        Bookingdates bookingdates = new Bookingdates("2024-01-01", "2024-01-20");
        Booking booking = new Booking("Vlada", "Terentieva", 200, false, bookingdates, "Baby crib");


        //get response
        Response response = RestAssured.given(spec).contentType(ContentType.JSON).
                body(booking).post("/booking");
        response.print();
        BookingId bookingId = response.as(BookingId.class);

        //verifications
        //verify response 200
        Assert.assertEquals(response.getStatusCode(), 200, "Status code expected to be 200 but it is not");

        System.out.println("Request booking: " + booking.toString());
        System.out.println("Response booking: " + bookingId.getBooking().toString());
        //verify all fields
        Assert.assertEquals(bookingId.getBooking().toString(),booking.toString());

    }
    @AfterClass(alwaysRun = true)
    public void deleteInitializedBooking() {
        if (response != null) {
            int bookingid = response.jsonPath().getInt("bookingid");

            //delete booking with authorization (auth().preemptive().basic("username","password").)
            Response responseDelete = RestAssured.given(spec).auth().preemptive().basic("admin", "password123")
                    .delete("/booking/" + bookingid);
            responseDelete.print();
        }
    }

}
