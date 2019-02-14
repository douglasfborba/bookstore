package com.matera.trainning.bookstore.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.isEmptyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
import com.matera.trainning.bookstore.service.ProdutoService;
import com.matera.trainning.bookstore.service.exception.RecursoAlreadyExistsException;

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

	@Before
	public void setUp() {				
		livroTheHobbit = new ProdutoDTO();
		livroTheHobbit.setCodigo("LIVRO23040");
		livroTheHobbit.setDescricao("Livro The Hobbit");
		livroTheHobbit.setPreco(new BigDecimal(57.63));
		livroTheHobbit.setDataCadastro(LocalDate.now());
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
				.andExpect(header().string("location", containsString("http://localhost/v1/produtos/")));
	}
	
	@Test
	public void insereProdutoDuplicado() throws Exception {
		when(produtoService.inserirProduto(Mockito.any(ProdutoDTO.class)))
			.thenThrow(RecursoAlreadyExistsException.class);		

		String jsonObject = jsonMapper.writeValueAsString(livroTheHobbit);
		mockMvc.perform(post("/v1/produtos")
				.contentType(APPLICATION_JSON_UTF8)
				.content(jsonObject))
				.andExpect(status().isConflict())
				.andExpect(header().string("location", containsString("http://localhost/v1/produtos/")))
				.andExpect(content().string(isEmptyString()));
	}
	
//	@Test
//	public void listaProdutosEmBancoPopulado() throws Exception {
//		String jsonArray = jsonMapper.writeValueAsString(list(dtoTheHobbit, dtoIt));
//
//		when(produtoService.findAll()).thenReturn(list(dtoTheHobbit, dtoIt));
//		mockMvc.perform(get("/produtos")
//					.accept(APPLICATION_JSON_UTF8))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$", hasSize(2)))
//				.andExpect(content().json(jsonArray))
//				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
//	}

//	@Test
//	public void listaProdutosEmBancoVazio() throws Exception {
//		String jsonArray = jsonMapper.writeValueAsString(list());
//
//		when(facade.findAll()).thenReturn(list());
//		mockMvc.perform(get("/produtos")
//					.accept(APPLICATION_JSON_UTF8))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$", hasSize(0)))
//				.andExpect(content().json(jsonArray))
//				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
//	}
//
//	@Test
//	public void buscaProdutoPeloCodigo() throws Exception {
//		String jsonObject = jsonMapper.writeValueAsString(dtoTheHobbit);
//
//		when(facade.findByCodigo(Mockito.anyString())).thenReturn(dtoTheHobbit);
//		mockMvc.perform(get("/produtos/{codigo}", COD_LIVRO_HOBBIT)
//					.accept(APPLICATION_JSON_UTF8))
//				.andExpect(status().isOk())
//				.andExpect(content().json(jsonObject))
//				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
//	}
//
//	@Test
//	public void buscaProdutoInexistentePeloCodigo() throws Exception {
//		when(facade.findByCodigo(Mockito.anyString())).thenThrow(RegistroNotFoundException.class);
//		mockMvc.perform(get("/produtos/{codigo}", COD_LIVRO_HOBBIT)
//					.accept(APPLICATION_JSON_UTF8))
//				.andExpect(status().isNotFound())
//				.andExpect(content().string(isEmptyString()));
//	}
//
//	@Test
//	public void buscaProdutoPelaDescricao() throws Exception {
//		String jsonArray = jsonMapper.writeValueAsString(list(dtoTheHobbit, dtoIt));
//
//		when(facade.findByDescricao(Mockito.anyString())).thenReturn(list(dtoTheHobbit, dtoIt));
//		mockMvc.perform(get("/produtos/search")
//					.accept(APPLICATION_JSON_UTF8)
//					.param("descricao", "LiVrO"))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$", hasSize(2)))
//				.andExpect(content().json(jsonArray))
//				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
//	}
//
//	@Test
//	public void buscaProdutoInexistentePelaDescricao() throws Exception {
//		String jsonArray = jsonMapper.writeValueAsString(list());
//
//		when(facade.findByDescricao(Mockito.anyString())).thenReturn(list());		
//		mockMvc.perform(get("/produtos/search")
//					.accept(APPLICATION_JSON_UTF8)
//					.param("descricao", "LiVrO"))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$", hasSize(0)))
//				.andExpect(content().json(jsonArray))
//				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
//	}
//
//	@Test
//	public void persisteProduto() throws Exception {
//		String jsonObject = jsonMapper.writeValueAsString(dtoTheHobbit);
//
//		when(facade.insert(Mockito.any(ProdutoDTO.class))).thenReturn(dtoTheHobbit);		
//		mockMvc.perform(post("/produtos")
//					.contentType(APPLICATION_JSON_UTF8)
//					.content(jsonObject))
//				.andExpect(status().isCreated())
//				.andExpect(content().json(jsonObject))
//				.andExpect(header().string("location", containsString("http://localhost/produtos/")))
//				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
//	}
//
//	@Test
//	public void persisteProdutoDuplicado() throws Exception {
//		String jsonObject = jsonMapper.writeValueAsString(dtoTheHobbit);
//
//		when(facade.insert(Mockito.any(ProdutoDTO.class))).thenThrow(RegistroAlreadyExistsException.class);		
//		mockMvc.perform(post("/produtos")
//					.contentType(APPLICATION_JSON_UTF8)
//					.content(jsonObject))
//				.andExpect(status().isConflict())
//				.andExpect(header().string("location", isEmptyOrNullString()))
//				.andExpect(content().string(isEmptyString()));
//	}
//
//	@Test
//	public void atualizaProduto() throws Exception {
//		String jsonObject = jsonMapper.writeValueAsString(dtoTheHobbit);
//
//		mockMvc.perform(put("/produtos/{codigo}", COD_LIVRO_HOBBIT)
//					.contentType(APPLICATION_JSON_UTF8)
//					.content(jsonObject))
//				.andExpect(status().isNoContent())
//				.andExpect(content().string(isEmptyString()));
//	}
//
//	@Test
//	public void atualizaProdutoInexistente() throws Exception {
//		String jsonObject = jsonMapper.writeValueAsString(dtoTheHobbit);
//
//		doThrow(new RegistroNotFoundException("Produto inexistente")).when(facade)
//			.update(Mockito.anyString(), Mockito.any(ProdutoDTO.class));
//		
//		mockMvc.perform(put("/produtos/{codigo}", COD_LIVRO_HOBBIT)
//					.contentType(APPLICATION_JSON_UTF8)
//					.content(jsonObject))
//				.andExpect(status().isNotFound())
//				.andExpect(content().string(isEmptyString()));
//	}
//
//	@Test
//	public void excluiProduto() throws Exception {
//		mockMvc.perform(delete("/produtos/{codigo}", COD_LIVRO_HOBBIT)
//					.accept(APPLICATION_JSON_UTF8))
//				.andExpect(status().isNoContent())
//				.andExpect(content().string(isEmptyString()));
//	}
//
//	@Test
//	public void excluiProdutoInexistente() throws Exception {
//		doThrow(new RegistroNotFoundException("Produto inexistente")).when(facade)
//			.delete(Mockito.anyString());
//
//		mockMvc.perform(delete("/produtos/{codigo}", COD_LIVRO_HOBBIT)
//					.accept(APPLICATION_JSON_UTF8))
//				.andExpect(status().isNotFound())
//				.andExpect(content().string(isEmptyString()));
//	}

}
