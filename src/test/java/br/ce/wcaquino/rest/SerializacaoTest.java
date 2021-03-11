package br.ce.wcaquino.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.http.ContentType;

public class SerializacaoTest {
	
	@Test
	public void deveSalvarUsuario_ComMap(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "new name");
		params.put("age", 44);
		
		given()
			.log().all()
			.contentType("application/json")
			// Comparar o a classe postTest
			.body(params)
		.when()
			.post("http://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("name", is("new name"))
			.body("id", is(notNullValue()))
		;
	}
	
	@Test
	public void deveSalvarUsuarioUsandoObjeto(){
		User user = new User("new user 2", 35);
		
		given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
			.post("http://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("name", is("new user 2"))
			.body("id", is(notNullValue()));
	}
	
	@Test
	public void deveDeserializarObjetoAoSalvarUsuario(){
		User user = new User("new user 2", 35);
		
		given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
			.post("http://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class);
		
		System.out.println(user);
		assertThat(user.getName(), is("new user 2"));
		
	}
	
	@Test
	public void deveSalvarUsuarioViaXMLUsandoObjeto(){
		// Aqui funciona devido as anotations na classe User e o contrutor vazio
		User user = new User("new user xml", 35);
		
		given()
			.log().all()
			.contentType(ContentType.XML)
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.body("user.age", is("35"));
	}

}
