package br.ce.wcaquino.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.xml.HasXPath.hasXPath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import io.restassured.http.ContentType;

public class PostTest {
	
	@Test
	public void deveSalvarUsuario_ComPost(){
		given()
			// Aqui vai monntar log no console
			.log().all()
			// Aqui deve informar que o tipo da requisicao eh json
			.contentType("application/json")
			.body("{\"name\": \"Jose\", \"age\": 50 }")
		.when()
			.post("http://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
		;
	}
	
	@Test
	public void naoDeveSalvarUsuarioSemNome(){
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"age\": 50 }")
		.when()
			.post("http://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(400)
			.body("id", is(nullValue()))
			.body("error", is("Name é um atributo obrigatório"))
		;
	}
	
	@Test
	public void DeveSalvarUsuarioComXML(){
		given()
		.log().all()
		.contentType(ContentType.XML)
		.body("<user><name>Jose</name><age>50</age></user>")
	.when()
		.post("http://restapi.wcaquino.me/usersXML")
	.then()
		.log().all()
		.statusCode(201)
		.rootPath("user")
		.body("id", is(notNullValue()))
		.body("age", is("50"))
		;
	}
	
	@Test
	public void deveAlterarrUsuario_ComPut(){
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\": \"new user\", \"age\": 30 }")
		.when()
			.put("http://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("new user"))
		;
	}
	
	@Test
	public void deveRemoverUsuario_ComDelete(){
		given()
			.log().all()
		.when()
			.delete("http://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(204)
		;
	}
	
	@Test
	public void naoDeveRemoverUsuario(){
		given()
			.log().all()
		.when()
			.delete("http://restapi.wcaquino.me/users/10000")
		.then()
			.log().all()
			.statusCode(400)
			.body("error", is("Registro inexistente"))
		;
	}
}
