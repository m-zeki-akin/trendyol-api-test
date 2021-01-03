package com.trendyolapi.test.integration.test;

import static io.restassured.RestAssured.*;

import io.restassured.parsing.Parser;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.*;

import com.trendyolapi.test.booklist.BookApplication;
import com.trendyolapi.test.booklist.BookRepository;

import io.restassured.RestAssured;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BookApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class APITestCase extends AbstractTestNGSpringContextTests {

	private static String API_ROOT;

	@Autowired
	BookRepository bookRepository;

	@Autowired
	Environment environment;

	@LocalServerPort
	int port;

	@BeforeMethod(alwaysRun = true, dependsOnMethods = {"springTestContextBeforeTestMethod"})
	protected void setUp(){

		API_ROOT = "http://localhost:"+port;
		RestAssured.port = port;
		RestAssured.defaultParser = Parser.JSON;
		logger.info(API_ROOT+"/api/books");

	}

	protected static String getRoot(){
		return API_ROOT;
	}

	protected void cleanDB(){

		when().
				delete(APITestCase.getRoot()+"/api/books").
		then().
				assertThat().statusCode(200).log().all();

	}
}
