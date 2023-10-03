package com.herokuapp.restfulbookers;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class GetBookingIdsTests extends BaseTest{
    @Test
    public void getBookingIdsWithoutFilterTest() {
        //Get response with booking ids
        Response response = RestAssured.given(spec).get("/booking");
        response.print();

        //verify response 200
        Assert.assertEquals(response.getStatusCode(), 200, "Status code expected to be 200 but it is not");
        //verify at least 1 booking id in response
        List<Integer> bookingIds = response.jsonPath().getList("bookingid");
        Assert.assertFalse(bookingIds.isEmpty(), "List of booking ids is empty but it should not be.");
    }

    @Test
    public void getBookingIdsWithFilterTest() {
        // add query parameter to spec
        spec.queryParam("firstname","Yuliia");
        spec.queryParam("lastname","Terentieva");

        //Get response with booking ids
        Response response = RestAssured.given(spec).get("/booking");
        response.print();

        //verify response 200
        Assert.assertEquals(response.getStatusCode(), 200, "Status code expected to be 200 but it is not");
        //verify at least 1 booking id in response
        List<Integer> bookingIds = response.jsonPath().getList("bookingid");
        Assert.assertFalse(bookingIds.isEmpty(), "List of booking ids is empty but it should not be.");
    }
}
