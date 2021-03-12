package br.ce.wcaquino.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.io.IOException;

import org.junit.Test;

public class AutenticacaoTest {
	
	@Test
	public void naoDeveAcessarSemSenha(){
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(401);
	}
	
	@Test
	public void deveFazerAutenticationBasic(){
		given()
			.log().all()
		.when()
			// Autenticando atraves da URL
			.get("https://admin:senha@restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
			;
	}
	
	@Test
	public void deveFazerAutenticationBasic2(){
		given()
			.log().all()
			.auth().basic("admin", "senha")
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
			;
	}

}
