package br.ce.wcaquino.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import com.sun.org.apache.xerces.internal.dom.NodeImpl;

public class XmlTest {

	@Test
	public void devoTrabalharComXml_PrimeiraForma() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/usersXML/3")
		.then()
			.statusCode(200)
			.body("user.name", is("Ana Julia"))
			.body("user.@id", is("3"))
			.body("user.filhos.name.size()", is(2))
			.body("user.filhos.name[0]", is("Zezinho"))
			.body("user.filhos.name[1]", is("Luizinho"))
			.body("user.filhos.name", hasItems("Luizinho"));
	
	}
	
	@Test
	public void devoTrabalharComXml_SegundaForma() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/usersXML/3")
		.then()
			.statusCode(200)
			.rootPath("user")
			.body("name", is("Ana Julia"))
			.body("@id", is("3"))
			
			.appendRootPath("filhos")
			.body("name.size()", is(2))
			.body("name[0]", is("Zezinho"))
			.body("name[1]", is("Luizinho"))
			.body("name", hasItems("Luizinho"));
	
	}
	
	@Test
	public void devoTrabalharComXml_TerceiraForma() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/usersXML")
		.then()
			.statusCode(200)
			.rootPath("users.user")
			.body("findAll{it.age.toInteger() <= 25}.size()", is(2))
			.body("@id", hasItems("1", "2", "3"))
			.body("findAll{it.age.toInteger() == 25}.name", is("Maria Joaquina"))
			.body("findAll{it.name.toString().contains('n')}.name", hasItems("Maria Joaquina", "Ana Julia"))
			.body("salary.find{it != null}.toDouble()", is(1234.5678d))
			.body("age.collect{it.toInteger() * 2}", hasItems(40, 50, 60))
			.body("name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}", is("MARIA JOAQUINA"));
	}
	
	@Test
	public void unindoJavaComXML_QuartaForma() {
		ArrayList<NodeImpl> nomes = given()
		.when()
			.get("http://restapi.wcaquino.me/usersXML")
		.then()
			.statusCode(200)
			.extract().path("users.user.name.findAll{it.toString().contains('n')}");
		;
		
		assertEquals(2, nomes.size());
		assertEquals("Maria Joaquina".toUpperCase(), nomes.get(0));
		assertTrue("ANA JULIA".equalsIgnoreCase(nomes.get(1).toString()));
	}

}
