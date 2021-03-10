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
		assertThat("Joaquina", allOf(startsWith("J"), endsWith("na"), containsString("oaqui")));
		
	}
	
	@Test
	public void devoValidarBody() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/ola")
		.then().
			statusCode(200)
			// Verificacao rigida com toda a resposta
			.body(is("Ola Mundo!"))
			// Verificacao flexivel
			.body(containsString("Ola"))
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
	
	@Test
	public void devoVerificarListaRaiz() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			
			// Verifica se na raiz tem 3 objetos ($ convencao da raiz)
			.body("$", hasSize(3))
			
			// Verifica se na lista tem os nome...
			.body("name", hasItems("João da Silva", "Ana Júlia"))
			
			// Verifica se tem uma idade igual a 25
			.body("age[1]", is(25))
			
			
			// Verifica se na lista possui outra lista com os nomes...
			.body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho")))
			
			
			.body("salary", contains(1234.5678f, 2500, null));
	}
	
	@Test
	public void deveFazerVerificacoesAvencadas() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			
			// Verifica se na raiz tem 3 objetos ($ convencao da raiz)
			.body("$", hasSize(3))
			
			// Verifica se na lista tem idade duas idades menor igual a...
			.body("age.findAll{it <= 25}.size()", is(2))
			
			// Verifica se na lsita tem uma idade idade menor que... e maior que...
			.body("age.findAll{it <= 25 && it > 20}.size()", is(1))
			
			// Verifica se na lsita tem idade menor que... e maior que... buscando o nome. 
			.body("findAll{it.age <= 25 && it.age > 20}.name", hasItem("Maria Joaquina"))
			
			// Verifica se na lista raiz se tem na primeira posicao alguem com idade menor igual a... buscando o nome
			.body("findAll{it.age <= 25}[0].name", is("Maria Joaquina"))
			
			// Verifica se na lista raiz se tem na primeira posicao do ultimo item ao primeiro, alguem com idade menor igual a... buscando o nome
			.body("findAll{it.age <= 25}[-1].name", is("Ana Júlia"))
			
			// Verifique se tem uma pessoa com idade menor igual a ... buscando nome
			.body("find{it.age <= 25}.name", is("Maria Joaquina"))
			
			// Verifique se tem pessoas duas pessoas com n no nome
			.body("findAll{it.name.contains('n')}.name", hasItems("Maria Joaquina", "Ana Júlia"))
			
			// Verifique nomes dois nome que sao maiores que 10 caracateres
			.body("findAll{it.name.length() > 10}.name", hasItems("Maria Joaquina", "João da Silva"))
			
			// Verifique na lista um nome e transforme para maiusculo
			.body("name.collect{it.toUpperCase()}", hasItems("MARIA JOAQUINA"))
			
			// Verifique um nome que comeca com Maria e transforme para maiusculo
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
			
			// Verifique um nome que comeca com Maria e transforme para maiusculo e o tamanho do array eh um
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()", allOf(arrayContaining("MARIA JOAQUINA"), arrayWithSize(1)))
			
			// Veririque as idades e multiplique por 2
			.body("age.collect{it * 2}", hasItems(60, 50, 40))
			
			// Verifique o id maximo na lista
			.body("id.max()", is(3))
			
			// Verifique o menor salario da lista
			.body("salary.min()", is(1234.5678f))
			
			// Verifique os salarios e some eles com uma margem de erro de 0.001
			.body("salary.findAll{it != null}.sum()", is(closeTo(3734.5678f, 0.001)))
			
			// Verique todos os salarios e some, devendo ser maior que 3000 e menor que 5000
			.body("salary.findAll{it != null}.sum()", allOf(greaterThan(3000d), lessThan(5000d)));
	}

}
