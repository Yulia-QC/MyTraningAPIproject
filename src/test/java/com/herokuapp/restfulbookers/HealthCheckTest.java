package com.herokuapp.restfulbookers;

import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class HealthCheckTest extends BaseTest {
    @Test
    public void healthCheckTest() {
        //BDD gherkin syntax
        given().
                spec(spec).
                when().
                get("/ping").
                then().
                assertThat().
                statusCode(201);

    }
    @Test
    public void headersAndCookiesTest() {
        //adding test cookies and headers
        Header someHeader = new Header("some header name","some header value");
//        spec.header(someHeader);

        Cookie someCookie = new Cookie.Builder("some cookie name","some cookie value").build();
//        spec.cookie(someCookie);

        Response response = RestAssured.given(spec).
                cookie("Test cookie name","Test cookie value").
                header("Test header name","Test header value").log().all().get("/ping");

        //getting headers
        Headers headers = response.getHeaders();
        System.out.println("Headers: " + headers);
//#1
        Header serverHeader1 = headers.get("Server");
        System.out.println(serverHeader1.getName() + ": " + serverHeader1.getValue());
//#2
        String serverHeader2 =  response.getHeader("Server");
        System.out.println("Server: " + serverHeader2);

        //getting cookies
        Cookies cookies = response.getDetailedCookies();
        System.out.println("Cookies: " + cookies);
    }
}
