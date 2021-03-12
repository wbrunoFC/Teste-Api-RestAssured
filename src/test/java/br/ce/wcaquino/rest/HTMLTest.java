package br.ce.wcaquino.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.xml.HasXPath;
import org.junit.Test;

import io.restassured.http.ContentType;

public class HTMLTest {

	@Test
	public void deveFazerBuscasComHTML() {
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/v2/users")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.HTML)
			.body("html.body.div.table.tbody.tr.size()", is(3))
			.body("html.body.div.table.tbody.tr[1].td[2]", is("25"))
			.appendRootPath("html.body.div.table.tbody")
			.body("tr.find{it.toString().startsWith('2')}. td[1]", is("Maria Joaquina"))
		;
	}
	
	@Test
	public void deveFazerBuscasComXpathEmHTML() {
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/v2/users?format=clean")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.HTML)
			.body(HasXPath.hasXPath("count(//table/tr)", is("4")))
			.body(HasXPath.hasXPath("//td[text() = '2']/../td[2]", is("Maria Joaquina")))
		;
	}
}
