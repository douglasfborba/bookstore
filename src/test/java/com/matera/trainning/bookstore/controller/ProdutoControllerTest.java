package com.matera.trainning.bookstore.controller;

import static org.assertj.core.util.Lists.list;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.isEmptyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matera.trainning.bookstore.model.Produto;
import com.matera.trainning.bookstore.service.ProdutoService;
import com.matera.trainning.bookstore.service.exceptions.RegistroAlreadyExistsException;
import com.matera.trainning.bookstore.service.exceptions.RegistroNotFoundException;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ProdutoController.class)
public class ProdutoControllerTest {

	private static final String COD_LIVRO_HOBBIT = "LIVRO23040";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper jsonMapper;

	@MockBean
	private ProdutoService service;

	private Produto livroTheHobbit;
	private Produto livroIt;

	@Before
	public void setUp() {
		jsonMapper = new ObjectMapper();
		livroTheHobbit = new Produto("LIVRO23040", "Livro The Hobbit", new BigDecimal(57.63), LocalDate.now());
		livroIt = new Produto("LIVRO34536", "Livro IT - A coisa", new BigDecimal(74.90), LocalDate.now());
	}

	@Test
	public void listaProdutosEmBancoPopulado() throws Exception {
		String jsonArray = jsonMapper.writeValueAsString(list(livroTheHobbit, livroIt));

		when(service.findAll()).thenReturn(list(livroTheHobbit, livroIt));
		mockMvc.perform(get("/produtos")
					.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(content().json(jsonArray))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}

	@Test
	public void listaProdutosEmBancoVazio() throws Exception {
		String jsonArray = jsonMapper.writeValueAsString(list());

		when(service.findAll()).thenReturn(list());
		mockMvc.perform(get("/produtos")
					.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)))
				.andExpect(content().json(jsonArray))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}

	@Test
	public void buscaProdutoPeloCodigo() throws Exception {
		String jsonObject = jsonMapper.writeValueAsString(livroTheHobbit);

		when(service.findByCodigo(Mockito.anyString())).thenReturn(livroTheHobbit);
		mockMvc.perform(get("/produtos/{codigo}", COD_LIVRO_HOBBIT)
					.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonObject))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}

	@Test
	public void buscaProdutoInexistentePeloCodigo() throws Exception {
		when(service.findByCodigo(Mockito.anyString())).thenThrow(RegistroNotFoundException.class);
		mockMvc.perform(get("/produtos/{codigo}", COD_LIVRO_HOBBIT)
					.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}

	@Test
	public void buscaProdutoPelaDescricao() throws Exception {
		String jsonArray = jsonMapper.writeValueAsString(list(livroTheHobbit, livroIt));

		when(service.findByDescricao(Mockito.anyString())).thenReturn(list(livroTheHobbit, livroIt));
		mockMvc.perform(get("/produtos/search")
					.accept(APPLICATION_JSON_UTF8)
					.param("descricao", "LiVrO"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(content().json(jsonArray))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}

	@Test
	public void buscaProdutoInexistentePelaDescricao() throws Exception {
		String jsonArray = jsonMapper.writeValueAsString(list());

		when(service.findByDescricao(Mockito.anyString())).thenReturn(list());		
		mockMvc.perform(get("/produtos/search")
					.accept(APPLICATION_JSON_UTF8)
					.param("descricao", "LiVrO"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)))
				.andExpect(content().json(jsonArray))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}

	@Test
	public void persisteProduto() throws Exception {
		String jsonObject = jsonMapper.writeValueAsString(livroTheHobbit);

		when(service.insert(Mockito.any(Produto.class))).thenReturn(livroTheHobbit);		
		mockMvc.perform(post("/produtos")
					.contentType(APPLICATION_JSON_UTF8)
					.content(jsonObject))
				.andExpect(status().isCreated())
				.andExpect(content().json(jsonObject))
				.andExpect(header().string("location", containsString("http://localhost/produtos/")))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}

	@Test
	public void persisteProdutoDuplicado() throws Exception {
		String jsonObject = jsonMapper.writeValueAsString(livroTheHobbit);

		when(service.insert(Mockito.any(Produto.class))).thenThrow(RegistroAlreadyExistsException.class);		
		mockMvc.perform(post("/produtos")
					.contentType(APPLICATION_JSON_UTF8)
					.content(jsonObject))
				.andExpect(status().isConflict())
				.andExpect(header().string("location", isEmptyOrNullString()))
				.andExpect(content().string(isEmptyString()));
	}

	@Test
	public void atualizaProduto() throws Exception {
		String jsonObject = jsonMapper.writeValueAsString(livroTheHobbit);

		mockMvc.perform(put("/produtos/{codigo}", COD_LIVRO_HOBBIT)
					.contentType(APPLICATION_JSON_UTF8)
					.content(jsonObject))
				.andExpect(status().isNoContent())
				.andExpect(content().string(isEmptyString()));
	}

	@Test
	public void atualizaProdutoInexistente() throws Exception {
		String jsonObject = jsonMapper.writeValueAsString(livroTheHobbit);

		doThrow(new RegistroNotFoundException("Produto inexistente")).when(service)
			.update(Mockito.anyString(), Mockito.any(Produto.class));
		
		mockMvc.perform(put("/produtos/{codigo}", COD_LIVRO_HOBBIT)
					.contentType(APPLICATION_JSON_UTF8)
					.content(jsonObject))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}

	@Test
	public void excluiProduto() throws Exception {
		mockMvc.perform(delete("/produtos/{codigo}", COD_LIVRO_HOBBIT)
					.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isNoContent())
				.andExpect(content().string(isEmptyString()));
	}

	@Test
	public void excluiProdutoInexistente() throws Exception {
		doThrow(new RegistroNotFoundException("Produto inexistente")).when(service)
			.delete(Mockito.anyString());

		mockMvc.perform(delete("/produtos/{codigo}", COD_LIVRO_HOBBIT)
					.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}

}
