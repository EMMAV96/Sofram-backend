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
	void openApiDocsArePublic() throws Exception {
		mockMvc.perform(get("/v3/api-docs"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi", not(blankOrNullString())));
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

	@Test
	void personalEndpointsRequireTokenAndAllowEmployeeTurnoFlow() throws Exception {
		String token = loginToken();

		mockMvc.perform(get("/personal/empleados"))
				.andExpect(status().isUnauthorized());

		String cargoResponse = mockMvc.perform(post("/personal/cargos")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "nombre": "Cuidador",
								  "sector": "Asistencia",
								  "matricula": null,
								  "especialidad": "Adultos mayores"
								}
								"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.nombre").value("Cuidador"))
				.andReturn()
				.getResponse()
				.getContentAsString();

		String cargoId = cargoResponse.replaceAll(".*\\\"id\\\":([0-9]+).*", "$1");

		String empleadoResponse = mockMvc.perform(post("/personal/empleados")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "cargoId": %s,
								  "apellido": "Perez",
								  "nombre": "Ana",
								  "dni": "30111222",
								  "fechaNacimiento": "1985-05-20",
								  "direccion": "Calle 123",
								  "telefono": "1122334455",
								  "email": "ana.perez@sofram.local"
								}
								""".formatted(cargoId)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.dni").value("30111222"))
				.andExpect(jsonPath("$.activo").value(true))
				.andReturn()
				.getResponse()
				.getContentAsString();

		String empleadoId = empleadoResponse.replaceAll(".*\\\"id\\\":([0-9]+).*", "$1");

		mockMvc.perform(get("/personal/turnos")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].descripcion").value("MANANA"));

		mockMvc.perform(post("/personal/empleados/" + empleadoId + "/asignaciones-turno")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "turnoId": 1,
								  "fechaDesde": "2026-09-04",
								  "motivoCambio": "Alta inicial"
								}
								"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.empleadoId").value(Integer.parseInt(empleadoId)))
				.andExpect(jsonPath("$.turnoDescripcion").value("MANANA"));
	}

	private String loginToken() throws Exception {
		String loginResponse = mockMvc.perform(post("/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "username": "admin",
								  "password": "admin123"
								}
								"""))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		return loginResponse.replaceAll(".*\\\"accessToken\\\":\\\"([^\\\"]+)\\\".*", "$1");
	}

}
