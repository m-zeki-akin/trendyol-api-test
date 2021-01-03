package com.trendyolapi.test.integration.test.tests;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.trendyolapi.test.integration.test.APITestCase;


public class MySuite extends APITestCase {




    @Test(priority = 1, groups = {"regular"})
    void verifyThatTheApiStartsWithAnEmptyStore() {
        cleanDB();

        when().
                get(getRoot()+"/api/books").
        then().
                assertThat().body("", hasSize(0)).
        and().
                log().all();

    }

    @Parameters({"author","title"})
    @Test(priority = 2, groups = {"regular"})
    void verifyThatTheTitleAndTheAuthorAreRequiredFields(String author, String title) throws JSONException {
        cleanDB();
        // title property requirement test
        JSONObject requestParams = new JSONObject();
        requestParams.put("author", author);

        given().
                header("Content-Type", "application/json").
                body(requestParams.toJSONString()).log().all().
        when().
                post(getRoot()+"/api/books").
        then().
                assertThat().statusCode(400).log().all().
        and().
                body("error",equalTo("Field 'title' is required"));

        // author property requirement test
        requestParams.clear();
        requestParams.put("title", title);

        given().
                header("Content-Type", "application/json").
                body(requestParams.toJSONString()).log().all().
        when().
                post(getRoot()+"/api/books").
        then().
                assertThat().statusCode(400).log().all().
        and().
                body("error",equalTo("Field 'author' is required"));

    }

    @Parameters({"author","title"})
    @Test(priority = 3, groups = {"regular"})
    void verifyThatTheTitleAndTheAuthorCannotBeEmpty(String author, String title) {
        cleanDB();
        // title property requirement test
        JSONObject requestParams = new JSONObject();
        requestParams.put("author", author);
        requestParams.put("title", " ");

        given().
                header("Content-Type", "application/json").
                body(requestParams.toJSONString()).log().all().
        when().
                post(getRoot()+"/api/books").
        then().
                assertThat().statusCode(400).log().all().
        and().
                body("error",equalTo("Field 'title' cannot be empty")).log().all();

        // author property requirement test
        requestParams.clear();
        requestParams.put("author", " ");
        requestParams.put("title", title);

        given().
                header("Content-Type", "application/json").
                body(requestParams.toJSONString()).log().all().
        when().
                post(getRoot()+"/api/books").
        then().
                assertThat().statusCode(400).
        and().
                body("error",equalTo("Field 'author' cannot be empty")).log().all();

    }

    @Parameters({"author","title"})
    @Test(priority = 4, groups = {"regular"})
    void verifyThatTheIdFieldIsReadOnly(String author, String title) {
        cleanDB();
        //add book first
        JSONObject requestParams = new JSONObject();
        requestParams.put("author", author);
        requestParams.put("title",title);

        given().
                header("Content-Type", "application/json").
                body(requestParams.toJSONString()).log().all().
        when().
                post(getRoot()+"/api/books").
        then().
                assertThat().statusCode(200);

        requestParams.clear();
        requestParams.put("id",20);

        given().
                header("Content-Type", "application/json").
                body(requestParams.toJSONString()).log().all().
        when().
                put(getRoot()+"/api/books").
        then().
                assertThat().statusCode(500).log().all();

    }

    @Parameters({"author","title"})
    @Test(priority = 5, groups = {"regular"})
    void verifyThatYouCanCreateNewBookViaPut(String author, String title) {
        cleanDB();

        JSONObject requestParams = new JSONObject();
        requestParams.put("author", author);
        requestParams.put("title", title);

        given().
                header("Content-Type", "application/json").
                body(requestParams.toJSONString()).log().all().
        when().
                post(getRoot()+"/api/books").
        then().
                assertThat().statusCode(200);

        requestParams.put("id",1);

        when().
                get(getRoot()+"/api/books/"+requestParams.get("id")).
        then().
                assertThat().statusCode(200).
        and().
                body("id",equalTo(requestParams.get("id"))).
                body("author",equalTo(requestParams.get("author"))).
                body("title",equalTo(requestParams.get("title")));

    }

    @Parameters({"author","title"})
    @Test(priority = 6, groups = {"regular"})
    void verifyThatYouCannotCreateDuplicateBook(String author, String title) {
        cleanDB();

        JSONObject requestParams = new JSONObject();
        requestParams.put("author", author);
        requestParams.put("title", title);

        given().
                header("Content-Type", "application/json").
                body(requestParams.toJSONString()).log().all().
        when().
                post(getRoot()+"/api/books").
        then().
                assertThat().statusCode(200).log().all();

        // try to add again
        given().
                header("Content-Type", "application/json").
                body(requestParams.toJSONString()).log().all().
        when().
                post(getRoot()+"/api/books").
        then().
                assertThat().statusCode(400).
        and().
                body("error",equalTo("Another book with similar title and author already exists")).log().all();
    }

}
