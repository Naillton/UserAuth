package com.nailton.UserAuth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nailton.UserAuth.model.User;
import com.nailton.UserAuth.service.UserService;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class UserAuthApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserService userService;

	@Test
	void insertUser() throws Exception {
		// instanciando um novo usuario
		final var testing = new User("Zeca", "zequiha@gmail.com", "zecaOhMaioral");
		// criando um objectMapper para   o usuario criado em um json
		final var objectMapper = new ObjectMapper();
		final var json = objectMapper.writeValueAsString(testing);
		final var request = mockMvc.perform(
				post("/api/cadastro")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json)
						.characterEncoding("utf-8"));
		request.andExpect(status().isOk()).andReturn();
	}

	@Test
	void findAllUsers() throws Exception {
		final var request = mockMvc.perform(get("/api/users"));
		request.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	void findUserById() throws Exception {
		User user = userService.listUsers().stream().findFirst().get();
		System.out.println(user.getId());
		final var request = mockMvc.perform(get("/api/users/"+user.getId()));
		request.andExpect(status().isOk()).andReturn();
	}


	@Test
	void delAllUsers() throws Exception {
		final var request = mockMvc.perform(delete("/api/delete"));
		request.andExpect(status().isAccepted())
				.andExpect(content().string("All/Users/Deleted"));
	}

}
