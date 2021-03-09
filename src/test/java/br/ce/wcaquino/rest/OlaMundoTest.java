package br.ce.wcaquino.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

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

}
