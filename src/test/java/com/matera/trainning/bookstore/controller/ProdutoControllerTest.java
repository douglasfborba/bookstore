package com.matera.trainning.bookstore.controller;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.matera.trainning.bookstore.service.ProdutoService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = { ProdutoController.class })
public class ProdutoControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ProdutoService service;

	@Test
	public void listaTodosOsProdutosSalvos() {
		fail("Not yet implemented");
	}

}
