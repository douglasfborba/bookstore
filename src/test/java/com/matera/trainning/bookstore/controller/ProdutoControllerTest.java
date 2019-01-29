package com.matera.trainning.bookstore.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.matera.trainning.bookstore.model.Produto;
import com.matera.trainning.bookstore.service.ProdutoService;
import com.matera.trainning.bookstore.service.exceptions.ProdutoAlreadyExistsException;
import com.matera.trainning.bookstore.service.exceptions.ProdutoNotFoundException;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ProdutoController.class, secure = false)
public class ProdutoControllerTest {

	private static final String JSON_TEMPLATE = "{" + "\"codigo\":\"LIVRO23040\","
			+ "\"descricao\":\"Livro The Hobbit\"" + ",\"preco\":57.63," + "\"dataCadastro\":\"29-01-2019\"}";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProdutoService service;

	private Produto livroTheHobbit;

	@Before
	public void setUp() {
		livroTheHobbit = new Produto("LIVRO23040", "Livro The Hobbit", new BigDecimal(57.63),
				LocalDate.of(2019, 01, 29));
	}

	@Test
	public void listaProdutosEmBanco() throws Exception {
		String uri = "/produtos";

		Mockito.when(service.findAll()).thenReturn(list(livroTheHobbit));
		MvcResult resultado = mockMvc.perform(get(uri).accept(APPLICATION_JSON)).andReturn();

		assertThat(resultado.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
		assertEquals("[" + JSON_TEMPLATE + "]", resultado.getResponse().getContentAsString(), false);
	}

	@Test
	public void listaProdutosEmBancoVazio() throws Exception {
		String uri = "/produtos";

		Mockito.when(service.findAll()).thenReturn(new ArrayList<Produto>());
		MvcResult resultado = mockMvc.perform(get(uri).accept(APPLICATION_JSON)).andReturn();

		assertThat(resultado.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
		assertEquals("[ ]", resultado.getResponse().getContentAsString(), false);
	}

	@Test
	public void buscaProdutoPeloCodigo() throws Exception {
		String uri = "/produtos/LIVRO23040";

		Mockito.when(service.findByCodigo(Mockito.anyString())).thenReturn(livroTheHobbit);
		MvcResult resultado = mockMvc.perform(get(uri).accept(APPLICATION_JSON)).andReturn();

		assertThat(resultado.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
		assertEquals(JSON_TEMPLATE, resultado.getResponse().getContentAsString(), false);
	}

	@Test
	public void buscaProdutoInexistentePeloCodigo() throws Exception {
		String uri = "/produtos/LIVRO23040";

		Mockito.when(service.findByCodigo(Mockito.anyString())).thenThrow(ProdutoNotFoundException.class);
		MvcResult resultado = mockMvc.perform(get(uri).accept(APPLICATION_JSON)).andReturn();

		assertThat(resultado.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
		assertThat(resultado.getResponse().getContentAsString()).isEmpty();
	}

	@Test
	public void buscaProdutoPelaDescricao() throws Exception {
		String uri = "/produtos/search?descricao=HoBbiT";

		Mockito.when(service.findByDescricao(Mockito.anyString())).thenReturn(list(livroTheHobbit));
		MvcResult resultado = mockMvc.perform(get(uri).accept(APPLICATION_JSON)).andReturn();

		assertThat(resultado.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
		assertEquals("[" + JSON_TEMPLATE + "]", resultado.getResponse().getContentAsString(), false);
	}

	@Test
	public void buscaProdutoInexistentePelaDescricao() throws Exception {
		String uri = "/produtos/search?descricao=HoBbiT";

		Mockito.when(service.findByDescricao(Mockito.anyString())).thenReturn(new ArrayList<Produto>());
		MvcResult resultado = mockMvc.perform(get(uri).accept(APPLICATION_JSON)).andReturn();

		assertThat(resultado.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
		assertEquals("[ ]", resultado.getResponse().getContentAsString(), false);
	}

	@Test
	public void salvaProduto() throws Exception {
		String uri = "/produtos";

		Mockito.when(service.insert(Mockito.any(Produto.class))).thenReturn(livroTheHobbit);
		MvcResult resultado = mockMvc
				.perform(post(uri).accept(APPLICATION_JSON).content(JSON_TEMPLATE).contentType(APPLICATION_JSON))
				.andReturn();

		assertThat(resultado.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
		assertEquals(JSON_TEMPLATE, resultado.getResponse().getContentAsString(), false);
	}

	@Test
	public void salvaProdutoDuplicado() throws Exception {
		String uri = "/produtos";

		Mockito.when(service.insert(Mockito.any(Produto.class))).thenThrow(ProdutoAlreadyExistsException.class);
		MvcResult resultado = mockMvc
				.perform(post(uri).accept(APPLICATION_JSON).content(JSON_TEMPLATE).contentType(APPLICATION_JSON))
				.andReturn();

		assertThat(resultado.getResponse().getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
		assertThat(resultado.getResponse().getContentAsString()).isEmpty();
	}

	@Test
	public void atualizaProduto() throws Exception {
		String uri = "/produtos/LIVRO23040";

		String template = JSON_TEMPLATE.replace("Livro The Hobbit", "Livro The Hobbit 2");
		MvcResult resultado = mockMvc
				.perform(put(uri).accept(APPLICATION_JSON).content(template).contentType(APPLICATION_JSON)).andReturn();

		assertThat(resultado.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
		assertThat(resultado.getResponse().getContentAsString()).isEmpty();
	}

	@Test
	public void atualizaProdutoInexistente() throws Exception {
		String uri = "/produtos/LIVRO23040";

		Mockito.when(service.updateByCodigo(Mockito.anyString(), Mockito.any(Produto.class)))
				.thenThrow(ProdutoNotFoundException.class);
		String template = JSON_TEMPLATE.replace("Livro The Hobbit", "Livro The Hobbit 2");
		MvcResult resultado = mockMvc
				.perform(put(uri).accept(APPLICATION_JSON).content(template).contentType(APPLICATION_JSON)).andReturn();

		assertThat(resultado.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
		assertThat(resultado.getResponse().getContentAsString()).isEmpty();
	}

	@Test
	public void deletaProduto() throws Exception {
		String uri = "/produtos/LIVRO23040";

		MvcResult resultado = mockMvc.perform(delete(uri).accept(APPLICATION_JSON)).andReturn();

		assertThat(resultado.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
		assertThat(resultado.getResponse().getContentAsString()).isEmpty();
	}

	@Test
	public void deletaProdutoInexistente() throws Exception {
		String uri = "/produtos/LIVRO23040";

		Mockito.when(service.deleteByCodigo(Mockito.anyString())).thenThrow(ProdutoNotFoundException.class);
		MvcResult resultado = mockMvc.perform(delete(uri).accept(APPLICATION_JSON)).andReturn();

		assertThat(resultado.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
		assertThat(resultado.getResponse().getContentAsString()).isEmpty();
	}

}
