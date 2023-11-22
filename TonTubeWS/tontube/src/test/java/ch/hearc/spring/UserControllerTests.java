package ch.hearc.spring;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	@Test
	@Order(1)
	public void shouldReturnOkMessage() throws Exception {
		String registerJson = "{\"username\":\"diogo1\",\"email\":\"diogo1@he-arc.ch\",\"password\":\"1234\"}";
		this.mockMvc.perform(post("/api/auth/register").contentType(APPLICATION_JSON_UTF8)
			.content(registerJson))
			.andDo(print())
			.andExpect(status().isOk());
		String loginJson = "{\"username\":\"diogo1\",\"password\":\"1234\"}";
		String responseJson = this.mockMvc.perform(post("/api/auth/login").contentType(APPLICATION_JSON_UTF8)
			.content(loginJson))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, String> token = objectMapper.readValue(responseJson, Map.class);
		this.mockMvc.perform(get("/api/auth/user")
			.header("Authorization", "Bearer " + token.get("token")))
			.andDo(print())
			.andExpect(status().isOk());
	}
	
	@Test
	@Order(2)
	public void shouldReturnErrorMessage() throws Exception {
		String registerJson = "{\"username\":\"diogo1\",\"email\":\"diogo1@he-arc.ch\"}";
		this.mockMvc.perform(post("/api/auth/register").contentType(APPLICATION_JSON_UTF8)
			.content(registerJson))
			.andDo(print())
			.andExpect(status().isOk());
		String loginJson = "{\"username\":\"diogo1\",\"password\":\"12345\"}";
		this.mockMvc.perform(post("/api/auth/login").contentType(APPLICATION_JSON_UTF8)
			.content(loginJson))
			.andDo(print())
			.andExpect(status().isOk());
		this.mockMvc.perform(get("/api/auth/user")
			.header("Authorization", "Bearer sdasdsad"))
			.andDo(print())
			.andExpect(status().isForbidden());
	}
}
