package com.matera.trainning.bookstore.controller;

import static org.assertj.core.util.Lists.list;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matera.trainning.bookstore.controller.dto.ComentarioDTO;
import com.matera.trainning.bookstore.controller.dto.ProdutoDTO;
import com.matera.trainning.bookstore.service.ProdutoService;
import com.matera.trainning.bookstore.service.exception.RecursoAlreadyExistsException;
import com.matera.trainning.bookstore.service.exception.RecursoNotFoundException;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ProdutoController.class)
public class ProdutoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper jsonMapper;
	
	@MockBean
	private ProdutoService produtoService;

	private ProdutoDTO livroTheHobbit;
	
	private ComentarioDTO hater;

	@Before
	public void setUp() {				
		livroTheHobbit = new ProdutoDTO();
		livroTheHobbit.setCodigo("LIVRO23040");
		livroTheHobbit.setDescricao("Livro The Hobbit");
		livroTheHobbit.setPreco(new BigDecimal(57.63));
		livroTheHobbit.setDataCadastro(LocalDate.now());
		
		hater = new ComentarioDTO();
		hater.setCodigo("dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1");
		hater.setDescricao("Odiei, este é o pior livro do mundo!");
		hater.setUsuario("usuario.hater");
		hater.setDataHoraCriacao(LocalDateTime.now());
	}

	@Test
	public void listaProdutosEmBasePopulada() throws Exception {
		Page<ProdutoDTO> produtos = new PageImpl<>(list(livroTheHobbit));
		
		when(produtoService.listarProdutos(Mockito.any(Pageable.class)))
			.thenReturn(produtos);
		
		String jsonArray = jsonMapper.writeValueAsString(produtos);
		mockMvc.perform(get("/v1/produtos")
				.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content", hasSize(1)))
				.andExpect(content().json(jsonArray))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void listaProdutosEmBaseVazia() throws Exception {
		Page<ProdutoDTO> produtos = new PageImpl<>(list());
		
		when(produtoService.listarProdutos(Mockito.any(Pageable.class)))
			.thenReturn(produtos);
		
		String jsonArray = jsonMapper.writeValueAsString(produtos);
		mockMvc.perform(get("/v1/produtos")
				.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content", hasSize(0)))
				.andExpect(content().json(jsonArray))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}

	@Test
	public void insereProduto() throws Exception {		
		when(produtoService.inserirProduto(Mockito.any(ProdutoDTO.class)))
			.thenReturn(livroTheHobbit);	
		
		String jsonObject = jsonMapper.writeValueAsString(livroTheHobbit);
		mockMvc.perform(post("/v1/produtos")
				.contentType(APPLICATION_JSON_UTF8)
				.content(jsonObject))
				.andExpect(status().isCreated())
				.andExpect(content().json(jsonObject))
				.andExpect(header().string("location", is("http://localhost/v1/produtos/" + livroTheHobbit.getCodigo())))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void insereProdutoDuplicado() throws Exception {
		when(produtoService.inserirProduto(Mockito.any(ProdutoDTO.class)))
			.thenThrow(new RecursoAlreadyExistsException(livroTheHobbit.getCodigo()));		

		String jsonObject = jsonMapper.writeValueAsString(livroTheHobbit);
		mockMvc.perform(post("/v1/produtos")
				.contentType(APPLICATION_JSON_UTF8)
				.content(jsonObject))
				.andExpect(status().isConflict())
				.andExpect(header().string("location", is("http://localhost/v1/produtos/" + livroTheHobbit.getCodigo())))
				.andExpect(content().string(isEmptyString()));
	}
	
	@Test
	public void atualizaProduto() throws Exception {
		String jsonObject = jsonMapper.writeValueAsString(livroTheHobbit);
		mockMvc.perform(put("/v1/produtos/{codProduto}", "LIVRO23040")
				.contentType(APPLICATION_JSON_UTF8)
				.content(jsonObject))
				.andExpect(status().isNoContent())
				.andExpect(content().string(isEmptyString()));
	}
	
	@Test
	public void atualizaProdutoInexistente() throws Exception {
		doThrow(RecursoNotFoundException.class)
			.when(produtoService).atualizarProduto(Mockito.anyString(), Mockito.any(ProdutoDTO.class));
		
		String jsonObject = jsonMapper.writeValueAsString(livroTheHobbit);
		mockMvc.perform(put("/v1/produtos/{codProduto}", "LIVRO23040")
				.contentType(APPLICATION_JSON_UTF8)
				.content(jsonObject))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}
	
	@Test
	public void removeProduto() throws Exception {
		mockMvc.perform(delete("/v1/produtos/{codProduto}", "LIVRO23040")
				.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isNoContent())
				.andExpect(content().string(isEmptyString()));
	}
	
	@Test
	public void removeProdutoInexistente() throws Exception {
		doThrow(RecursoNotFoundException.class).when(produtoService)
			.removerProduto(Mockito.anyString());
		
		mockMvc.perform(delete("/v1/produtos/{codProduto}", "LIVRO23040")
				.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}

	@Test
	public void buscaProdutoPeloCodigo() throws Exception {
		when(produtoService.buscarProdutoDadoCodigo(Mockito.anyString()))
			.thenReturn(livroTheHobbit);

		String jsonObject = jsonMapper.writeValueAsString(livroTheHobbit);
		mockMvc.perform(get("/v1/produtos/{codProduto}", "LIVRO23040")
				.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonObject))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}

	@Test
	public void buscaProdutoInexistentePeloCodigo() throws Exception {
		when(produtoService.buscarProdutoDadoCodigo(Mockito.anyString()))
			.thenThrow(RecursoNotFoundException.class);
		
		mockMvc.perform(get("/v1/produtos/{codigo}", "LIVRO23040")
				.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}

	@Test
	public void buscaProdutoPelaDescricao() throws Exception {
		Page<ProdutoDTO> produtos = new PageImpl<>(list(livroTheHobbit));

		when(produtoService.buscarProdutoDadoDescricao(Mockito.anyString(), Mockito.any(Pageable.class)))
			.thenReturn(produtos);

		String jsonArray = jsonMapper.writeValueAsString(produtos);
		mockMvc.perform(get("/v1/produtos")
				.accept(APPLICATION_JSON_UTF8)
				.param("descricao", "HoBbIt"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content", hasSize(1)))
				.andExpect(content().json(jsonArray))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}

	@Test
	public void buscaProdutoPelaDescricaoInexistente() throws Exception {
		Page<ProdutoDTO> produtos = new PageImpl<>(list());

		when(produtoService.buscarProdutoDadoDescricao(Mockito.anyString(), Mockito.any(Pageable.class)))
			.thenReturn(produtos);

		String jsonArray = jsonMapper.writeValueAsString(produtos);
		mockMvc.perform(get("/v1/produtos")
				.accept(APPLICATION_JSON_UTF8)
				.param("descricao", "LiVrO"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content", hasSize(0)))
				.andExpect(content().json(jsonArray))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));		
	}
	
	@Test
	public void listaComentariosPeloProduto() throws Exception {
		Page<ComentarioDTO> comentarios = new PageImpl<>(list(hater));

		when(produtoService.listarComentariosDadoCodigoProduto(Mockito.anyString(), Mockito.any(Pageable.class)))
			.thenReturn(comentarios);
		
		String jsonArray = jsonMapper.writeValueAsString(comentarios);
		mockMvc.perform(get("/v1/produtos/{codProduto}/comentarios", "dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1")
				.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content", hasSize(1)))
				.andExpect(content().json(jsonArray))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void listaComentariosPeloProdutoInexistente() throws Exception {
		when(produtoService.listarComentariosDadoCodigoProduto(Mockito.anyString(), Mockito.any(Pageable.class)))
			.thenThrow(RecursoNotFoundException.class);
	
		mockMvc.perform(get("/v1/produtos/{codProduto}/comentarios", "dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1")
			.accept(APPLICATION_JSON_UTF8))
			.andExpect(status().isNotFound())
			.andExpect(content().string(isEmptyString()));
	}

}
