package com.matera.trainning.bookstore.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ProdutoControllerTest {

	@Mock
	private MockMvc mvc;

	@InjectMocks
	private ProdutoController controller;

	@Test
	public void test() throws Exception {
		this.mvc.perform(get("/produtos")).andExpect(MockMvcResultMatchers.status().isOk());
	}

}
