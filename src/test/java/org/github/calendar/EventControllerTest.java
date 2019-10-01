package org.github.calendar;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void testEventController() throws Exception {
		
		//THIS DOES NOT COVER ALL POSSIBLE TESTS THAT CAN BE DONE!
		
		mockMvc.perform(get("/events").content("{}")).andDo(print()).andExpect(status().isOk());
		
		mockMvc.perform(post("/events").contentType(MediaType.APPLICATION_JSON).content("{\"name\": \"test\", \"length\": \"1\", \"date\": \"2019.09.29 13:13\"}")).andDo(print()).andExpect(status().isCreated());
		
		mockMvc.perform(get("/events").content("{}"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].name", is("test")))
			.andExpect(jsonPath("$[0].length", is(1)));
		
	}
	
}
