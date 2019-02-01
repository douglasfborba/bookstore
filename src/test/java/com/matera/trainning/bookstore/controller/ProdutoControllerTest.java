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
import com.matera.trainning.bookstore.controller.dto.ProdutoDTO;
import com.matera.trainning.bookstore.controller.facade.ProdutoFacade;
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
	private ProdutoFacade facade;

	private ProdutoDTO livroIt;
	private ProdutoDTO livroTheHobbit;

	@Before
	public void setUp() {
		jsonMapper = new ObjectMapper();
		
		livroIt = new ProdutoDTO();
		livroIt.setCodigo("LIVRO34536");
		livroIt.setDescricao("Livro IT - A coisa");
		livroIt.setPreco(new BigDecimal(74.90));
		livroIt.setDataCadastro(LocalDate.now());
		
		livroTheHobbit = new ProdutoDTO();
		livroTheHobbit.setCodigo("LIVRO23040");
		livroTheHobbit.setDescricao("Livro The Hobbit");
		livroTheHobbit.setPreco(new BigDecimal(57.63));
		livroTheHobbit.setDataCadastro(LocalDate.now());
	}

	@Test
	public void listaProdutosEmBancoPopulado() throws Exception {
		String jsonArray = jsonMapper.writeValueAsString(list(livroTheHobbit, livroIt));

		when(facade.findAll()).thenReturn(list(livroTheHobbit, livroIt));
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

		when(facade.findAll()).thenReturn(list());
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

		when(facade.findByCodigo(Mockito.anyString())).thenReturn(livroTheHobbit);
		mockMvc.perform(get("/produtos/{codigo}", COD_LIVRO_HOBBIT)
					.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonObject))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}

	@Test
	public void buscaProdutoInexistentePeloCodigo() throws Exception {
		when(facade.findByCodigo(Mockito.anyString())).thenThrow(RegistroNotFoundException.class);
		mockMvc.perform(get("/produtos/{codigo}", COD_LIVRO_HOBBIT)
					.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}

	@Test
	public void buscaProdutoPelaDescricao() throws Exception {
		String jsonArray = jsonMapper.writeValueAsString(list(livroTheHobbit, livroIt));

		when(facade.findByDescricao(Mockito.anyString())).thenReturn(list(livroTheHobbit, livroIt));
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

		when(facade.findByDescricao(Mockito.anyString())).thenReturn(list());		
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

		when(facade.insert(Mockito.any(ProdutoDTO.class))).thenReturn(livroTheHobbit);		
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

		when(facade.insert(Mockito.any(ProdutoDTO.class))).thenThrow(RegistroAlreadyExistsException.class);		
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

		doThrow(new RegistroNotFoundException("Produto inexistente")).when(facade)
			.update(Mockito.anyString(), Mockito.any(ProdutoDTO.class));
		
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
		doThrow(new RegistroNotFoundException("Produto inexistente")).when(facade)
			.delete(Mockito.anyString());

		mockMvc.perform(delete("/produtos/{codigo}", COD_LIVRO_HOBBIT)
					.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}

}
