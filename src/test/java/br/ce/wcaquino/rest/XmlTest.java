package br.ce.wcaquino.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.xml.HasXPath.hasXPath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.org.apache.xerces.internal.dom.NodeImpl;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class XmlTest {
	
	public static RequestSpecification request;
	public static ResponseSpecification response;
	
	@BeforeClass
	public static void setup() {
		// Com esse metodo estico o teste fica diponivel para rodar em qualquer ambiente
		RestAssured.baseURI = "http://restapi.wcaquino.me";
		
		// Nesse caso em especifico esses itens nao sao obrigatorios
//		RestAssured.basePath = "";
//		RestAssured.port = 443;
		
		RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
		reqBuilder.log(LogDetail.ALL);
		request = reqBuilder.build();
		
		ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
		// Com essa declaracao nao sera necessario colocar o status code no metodo
		resBuilder.expectStatusCode(200);
		response = resBuilder.build();
		
		RestAssured.requestSpecification = request;
		RestAssured.responseSpecification = response;
	}

	@Test
	public void devoTrabalharComXml_PrimeiraForma() {
		given()
		.when()
			.get("/usersXML/3")
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
			// Aqui eu poderia utilizar o metodo estatico
			.get("http://restapi.wcaquino.me/usersXML/3")
		.then()
		// Testando o beforeClass
//			.statusCode(200)
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
		assertEquals("Maria Joaquina".toUpperCase(), nomes.get(0).toString().toUpperCase());
		assertTrue("ANA JULIA".equalsIgnoreCase(nomes.get(1).toString()));
	}
	
	@Test
	public void trabalhandoComXPath_QuintaForma() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/usersXML")
		.then()
			.statusCode(200)
			
			// Verifique se no xml tem 3 usuarios
			.body(hasXPath("count(/users/user)", is("3")))
			
			// Verifique se tem um user com id 1
			.body(hasXPath("/users/user[@id = '1']"))
			.body(hasXPath("//user[@id = '2']"))
		
			// Verifique se no xml tem o nome Luizinho que traga o nome da mae Ana...
			.body(hasXPath("//name[text() = 'Luizinho']/../../name", is("Ana Julia")))
			
			// Verifique se no xml tem o nome Ana Julia com filhos fulano e ciclano...
			.body(hasXPath("//name[text() = 'Ana Julia']/following-sibling::filhos", allOf(containsString("Zezinho"), containsString("Luizinho"))))
			
			// Verifique se tem alguma ocorencia com o nome tal...
			.body(hasXPath("/users/user/name", is("João da Silva")))
			.body(hasXPath("//name", is("João da Silva")))
			
			// Verifique se na segunda posicao do XML tem o nome tal...
			.body(hasXPath("/users/user[2]/name", is("Maria Joaquina")))
			
			// Verifique se na ultima ocorrencia do user tem o nome tal...
			.body(hasXPath("/users/user[last()]/name", is("Ana Julia")))
			
			// Conte quantos usuarios tem n no nome e verifique se eh 2
			.body(hasXPath("count(/users/user/name[contains(., 'n')])", is("2")))
			
			// Verifique se tem algum user com idade menor que 24 e que traga o nome tal...
			.body(hasXPath("//user[age < 24]/name", is("Ana Julia")))
			
			// Verifique se tem algum user com idade maior que... e manor que... e que traga o nome tal...
			.body(hasXPath("//user[age > 20 and age < 30]/name", is("Maria Joaquina")))
			.body(hasXPath("//user[age > 20][age < 30]/name", is("Maria Joaquina")));
	}

}
