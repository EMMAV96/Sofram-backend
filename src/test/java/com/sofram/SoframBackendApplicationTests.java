package com.sofram;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SoframBackendApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void healthIsPublic() throws Exception {
		mockMvc.perform(get("/health"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("UP"));
	}

	@Test
	void protectedEndpointRequiresAuthentication() throws Exception {
		mockMvc.perform(get("/auth/me"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Autenticacion requerida"));
	}

	@Test
	void loginReturnsTokenAndTokenAllowsProtectedEndpoint() throws Exception {
		String loginResponse = mockMvc.perform(post("/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "username": "admin",
								  "password": "admin123"
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accessToken", not(blankOrNullString())))
				.andExpect(jsonPath("$.tokenType").value("Bearer"))
				.andExpect(jsonPath("$.username").value("admin"))
				.andExpect(jsonPath("$.rol").value("ADMINISTRADOR"))
				.andReturn()
				.getResponse()
				.getContentAsString();

		String token = loginResponse.replaceAll(".*\\\"accessToken\\\":\\\"([^\\\"]+)\\\".*", "$1");

		mockMvc.perform(get("/auth/me")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("admin"))
				.andExpect(jsonPath("$.rol").value("ADMINISTRADOR"));
	}

	@Test
	void invalidLoginReturnsUniformError() throws Exception {
		mockMvc.perform(post("/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "username": "admin",
								  "password": "incorrecta"
								}
								"""))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Credenciales invalidas"));
	}

}
