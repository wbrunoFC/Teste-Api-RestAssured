package br.ce.wcaquino.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

public class DownloadArquivoTest {
	
	@Test
	public void deveobrigarEnvioDeArquivo() throws IOException {
		byte[] image = 
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/download")
		.then()
			.statusCode(200)
			.extract().asByteArray();
		
		File imagem = new File("src/main/resources/file.jpg");
		OutputStream out = new FileOutputStream(imagem);
		out.write(image);
		out.close();
		
		Assert.assertThat(imagem.length(), lessThan(100000L));
	}

}
