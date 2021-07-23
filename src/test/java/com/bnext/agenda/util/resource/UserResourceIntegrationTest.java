package com.bnext.agenda.util.resource;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.bnext.agenda.data.dto.UserCreateDTO;
import com.bnext.agenda.util.BaseIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.restassured.http.ContentType;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Sql(scripts = "/db/insertUser.sql")
//@ContextConfiguration(classes = {WireMockConfig.class})
class UserResourceIntegrationTest extends BaseIntegrationTest{

	@LocalServerPort
	private int port;
	
	private ObjectMapper objectMapper;
	private String userForCreate;
	
	@BeforeEach
	void setUp() throws IOException {
		this.objectMapper = new ObjectMapper();
		this.objectMapper.registerModule(new JavaTimeModule());
		
		InputStream jsonUser = UserCreateDTO.class.getResourceAsStream("/db/user.json");
		userForCreate = IOUtils.toString(jsonUser, StandardCharsets.UTF_8.name());
	}
	
	@Test
	void teste() {
		given()
			.basePath("api/v1/users")
			.accept(ContentType.JSON)
			.port(port)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	
	@Test
	@DisplayName("Should create a new user")
	void shouldCreateAnewUser() {
		given()
			.basePath("api/v1/users")
			.contentType(ContentType.JSON)
			.port(port)
			.body(userForCreate)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}
	
	
	
	
}
