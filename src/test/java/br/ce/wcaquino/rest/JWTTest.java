package br.ce.wcaquino.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.http.ContentType;

public class JWTTest {
	
	@Test
	public void deveFazerAutenticationComToKenJsonWebToken(){
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "wagner@aquino");
		login.put("senha", "123456");
		
		// LOgin na api
		String token =
		given()
			.log().all()
			.body(login)
			.contentType(ContentType.JSON)
		.when()
			.post("https://barrigarest.wcaquino.me/signin")
		.then()
			.log().all()
			.statusCode(200)
			.extract().path("token");
		
		
		// Recebero token
		given()
			.log().all()
			.header("Authorization", "JWT" + token)
		.when()
			.get("https://barrigarest.wcaquino.me/contas")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", hasItem("Conta de teste"));
		
		// Obter as contas
	}

}
