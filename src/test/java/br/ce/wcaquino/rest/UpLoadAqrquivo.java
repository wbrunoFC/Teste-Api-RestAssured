package br.ce.wcaquino.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.io.File;

import org.hamcrest.xml.HasXPath;
import org.junit.Test;

import io.restassured.http.ContentType;

public class UpLoadAqrquivo {
	
	@Test
	public void deveobrigarEnvioDeArquivo() {
		given()
			.log().all()
		.when()
			.post("https://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(404)
			.body("error", is("Arquivo não enviado"))
		;
	}
	
//	@Test
//	public void deveFazerUploadDoArquivo() {
//		given()
//			.log().all()
//			.multiPart("arquivo", new File("src/main/resources/users.pdf"))
//		.when()
//			.get("https://restapi.wcaquino.me/upload")
//		.then()
//			.log().all()
//			.statusCode(200)
//			.body("name", is("users.pdf"))
//		;
//	}

}
