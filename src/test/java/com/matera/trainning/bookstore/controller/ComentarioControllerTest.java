package com.matera.trainning.bookstore.controller;

import static org.assertj.core.util.Lists.list;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matera.trainning.bookstore.controller.dto.ComentarioDTO;
import com.matera.trainning.bookstore.controller.dto.ProdutoDTO;
import com.matera.trainning.bookstore.controller.facade.ComentarioFacade;
import com.matera.trainning.bookstore.service.exceptions.RegistroNotFoundException;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ComentarioController.class)
public class ComentarioControllerTest {

	private static final String COD_USU_HATER = "dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper jsonMapper;

	@MockBean
	private ComentarioFacade facade;

	private ComentarioDTO hater;
	private ComentarioDTO lover;

	@Before
	public void setUp() {		
		ProdutoDTO livroTheHobbit = new ProdutoDTO();
		livroTheHobbit.setCodigo("LIVRO23040");
		livroTheHobbit.setDescricao("Livro The Hobbit");
		livroTheHobbit.setPreco(new BigDecimal(57.63));
		livroTheHobbit.setDataCadastro(LocalDate.now());

		hater = new ComentarioDTO();
		hater.setCodigo("dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1");
		hater.setDescricao("Odiei, este é o pior livro do mundo!");
		hater.setUsuario("usuario.hater");
		hater.setProduto(livroTheHobbit);
		hater.setDataHoraCriacao(LocalDateTime.now());
		
		lover = new ComentarioDTO();
		lover.setCodigo("dXN1YXJpby5sb3ZlcjMwMDEyMDE5MTcyNTAw");
		lover.setDescricao("Amei, melhor livro do mundo :D");
		lover.setUsuario("usuario.lover");
		lover.setProduto(livroTheHobbit);
		lover.setDataHoraCriacao(LocalDateTime.now());
	}

	@Test
	public void buscaComentarioInexistentePeloCodigo() throws Exception {
		when(facade.findByCodigo(Mockito.anyString())).thenThrow(RegistroNotFoundException.class);
		mockMvc.perform(get("/comentarios/{codigo}", COD_USU_HATER)
					.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}

	@Test
	public void buscaComentarioPeloProdutoCodigoAndDescricao() throws Exception {
		String jsonArray = jsonMapper.writeValueAsString(list(hater, lover));

		when(facade.findByProdutoCodigoAndDescricao(Mockito.anyString(), Mockito.anyString())).thenReturn(list(hater, lover));
		mockMvc.perform(get("/produtos/{codigoProduto}/comentarios/search", "LIVRO23040")
					.accept(APPLICATION_JSON_UTF8)
					.param("descricao", "LiVrO"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(content().json(jsonArray))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}

	@Test
	public void buscaComentarioInexistentePeloProdutoCodigoAndDescricao() throws Exception {
		String jsonArray = jsonMapper.writeValueAsString(list());

		when(facade.findByProdutoCodigoAndDescricao(Mockito.anyString(), Mockito.anyString())).thenReturn(list());
		mockMvc.perform(get("/produtos/{codigoProduto}/comentarios/search","LIVRO23040")
					.accept(APPLICATION_JSON_UTF8)
					.param("descricao", "LiVrO"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)))
				.andExpect(content().json(jsonArray))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void buscaComentarioPeloProdutoCodigoAndCodigo() throws Exception {
		String jsonObject = jsonMapper.writeValueAsString(hater);

		when(facade.findByProdutoCodigoAndCodigo(Mockito.anyString(),Mockito.anyString())).thenReturn(hater);
		mockMvc.perform(get("/produtos/{codigoProduto}/comentarios/{codigoComentario}", "LIVRO23040", COD_USU_HATER)
					.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonObject))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void buscaComentarioInexistentePeloProdutoCodigoAndCodigo() throws Exception {
		doThrow(new RegistroNotFoundException("Comentário inexistente para o produto informado")).when(facade)
			.findByProdutoCodigoAndCodigo(Mockito.anyString(), Mockito.anyString());

		mockMvc.perform(get("/produtos/{codigoProduto}/comentarios/{codigoComentario}", "LIVRO23040", COD_USU_HATER)
					.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}

	@Test
	public void persisteComentario() throws Exception {
		String jsonObject = jsonMapper.writeValueAsString(hater);

		when(facade.insert(Mockito.anyString(), Mockito.any(ComentarioDTO.class))).thenReturn(hater);
		mockMvc.perform(post("/produtos/{codigoProduto}/comentarios", "LIVRO23040")
					.contentType(APPLICATION_JSON_UTF8)
					.content(jsonObject))
				.andExpect(status().isCreated())
				.andExpect(content().json(jsonObject))
				.andExpect(header().string("location", containsString("http://localhost/produtos/" + hater.getProduto().getCodigo() + "/comentarios/" + hater.getCodigo())))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}

	@Test
	public void atualizaComentario() throws Exception {
		String jsonObject = jsonMapper.writeValueAsString(hater);

		mockMvc.perform(put("/produtos/{codigoProduto}/comentarios/{codigoComentario}", "LIVRO23040", COD_USU_HATER)
					.contentType(APPLICATION_JSON_UTF8)
					.content(jsonObject))
				.andExpect(status().isNoContent())
				.andExpect(content().string(isEmptyString()));
	}

	@Test
	public void atualizaComentarioInexistente() throws Exception {
		String jsonObject = jsonMapper.writeValueAsString(hater);

		doThrow(new RegistroNotFoundException("Comentário inexistente para o produto informado")).when(facade)
			.update(Mockito.anyString(), Mockito.anyString(), Mockito.any(ComentarioDTO.class));

		mockMvc.perform(put("/produtos/{codigoProduto}/comentarios/{codigoComentario}", "LIVRO23040", COD_USU_HATER)
					.contentType(APPLICATION_JSON_UTF8)
					.content(jsonObject))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}

	@Test
	public void excluiComentario() throws Exception {
		mockMvc.perform(delete("/produtos/{codigoProduto}/comentarios/{codigoComentario}", "LIVRO23040", COD_USU_HATER)
					.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isNoContent())
				.andExpect(content().string(isEmptyString()));
	}

	@Test
	public void excluirComentarioInexistente() throws Exception {
		doThrow(new RegistroNotFoundException("Comentário inexistente para o produto informado")).when(facade)
			.delete(Mockito.anyString(), Mockito.anyString());

		mockMvc.perform(delete("/produtos/{codigoProduto}/comentarios/{codigoComentario}", "LIVRO23040", COD_USU_HATER)
					.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}

}
