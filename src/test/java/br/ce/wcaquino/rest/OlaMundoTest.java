package br.ce.wcaquino.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class OlaMundoTest {

	@Test
	public void testDeRequisicao() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/ola")
		.then().
			statusCode(200);
	}
	
	@Test
	@SuppressWarnings("deprecation")
	public void conhendoMatchers() {
		assertThat("Maria", is("Maria"));

		// Verifique se o numero eh do tipo inteiro 
		assertThat(128, isA(Integer.class));
		
		// Verifique se o numero eh decimal
		assertThat(128d, isA(Double.class));
		
		// Verifique se 128 eh maior que...
		assertThat(128, greaterThan(120));
		assertThat(128d, greaterThan(120d));
		
		// Verifique se o numero eh menor que...
		assertThat(128, lessThan(150));
		assertThat(128d, lessThan(150d));
		
		List<Integer> listaDeInteiros = Arrays.asList(1, 2, 3, 4, 5);
		
		// Verifique se na lista contem 5 elementos
		assertThat(listaDeInteiros, hasSize(5));
		
		// Verifique se na lista contem tais valores(Precisa colcoar todos os elementos da lista)
		assertThat(listaDeInteiros, contains(1,2,3,4,5));
		
		// Verifique se na lista contem tais valores independente da ordem (Precisa colcoar todos os elementos da lista)
		assertThat(listaDeInteiros, containsInAnyOrder(5,4,3,2,1));
		
		// Verifique elemento unico na lista
		assertThat(listaDeInteiros, hasItem(3));
		
		// Verifique mais de um elemento na lista de forma unica
		assertThat(listaDeInteiros, hasItems(2,3));
		
		// Verifique se algo nao eh  igual a outro
		assertThat("Maria", is(not("marcos")));
		
		// Verifique se o nome Maria eh igual a Maria ou se Jose eh igual a Jose
		assertThat("Maria", anyOf(is("Jose"), is("Maria")));
		assertThat("Jose", anyOf(is("Jose"), is("Maria")));
		
		// Verifique se o nome comeca com... && temina com.... && contem...
		assertThat("Joaquina", allOf(startsWith("m"), endsWith("ia"), containsString("ar")));
		
	}
	
	@Test
	public void devoValidarBody() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/ola")
		.then().
			statusCode(200)
			// Verificacao rigida com toda a resposta
			.body(is("Ola, Mundo!"))
			// Verificacao flexivel
			.body(contains("Ola"))
			// Verificacao generica (Verifique se a respota nao esta vazia)
			.body(is(not(nullValue())));
		
	}
	
	@Test
	public void devoValidarBodyPrimeiroNivel() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/1")
		.then()
			.statusCode(200)
			.body("id", is(1))
			.body("name", containsString("Silva"))
			.body("age", greaterThan(17));
	}
	
	@Test
	public void devoValidarBodyPrimeiroNivelOutrasFormas() {
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/users/1");
		
		// Utilizando o path
		assertEquals(new Integer(1), response.path("id"));
		
		// Utilizando o JsonPath
		JsonPath jsonPath = new JsonPath(response.asString());
		assertEquals(1, jsonPath.getInt("id"));
		
		// utilizando o from
		int id = JsonPath.from(response.asString()).getInt("id");
		assertEquals(1, id);
		
		
	}
	

	@Test
	public void devoValidarBodySegundoNivel() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/2")
		.then()
			.statusCode(200)
			.body("name", containsString("Joaquina"))
			// Acessando o segundo nivel do json
			.body("endereco.rua", containsString("bobos"));
	}
	
	@Test
	public void devoVerificarlista() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/3")
		.then()
			.statusCode(200)
			.body("name", containsString("Ana"))
			
			// Vericando o tamanho da lsita por nivel
			.body("filhos", hasSize(2))
			
			// Verificando a primeira posicao
			.body("filhos[0].name", is("Zezinho"))
			
			// Verificando a segunda posicao
			.body("filhos[1].name", is("Luizinho"));
	}
	
	@Test
	public void devoVerificarErro() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/4")
		.then()
			.statusCode(404)
			.body("error", is("Usuário inexistente"));
	}

}
