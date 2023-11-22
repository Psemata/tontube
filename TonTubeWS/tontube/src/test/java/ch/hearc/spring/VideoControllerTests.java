package ch.hearc.spring;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class VideoControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void shouldReturnOkMessage() throws Exception {
		this.mockMvc.perform(get("/api/video")).andExpect(status().isOk());
		this.mockMvc.perform(get("/api/video/4cjWtqMF1WQ")).andExpect(status().isOk());
		this.mockMvc.perform(get("/api/video/ah8192hriuhf")).andExpect(status().isOk());
		this.mockMvc.perform(get("/api/video/user/diogo")).andExpect(status().isOk());
		this.mockMvc.perform(get("/api/video/user/dsasd")).andExpect(status().isOk());
	}
}
