package com.matera.trainning.bookstore.controller;

import static org.assertj.core.util.Lists.list;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.Base64Utils.encodeToString;

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
import com.matera.trainning.bookstore.controller.dto.AvaliacaoDTO;
import com.matera.trainning.bookstore.controller.dto.ComentarioDTO;
import com.matera.trainning.bookstore.controller.dto.ProdutoDTO;
import com.matera.trainning.bookstore.service.ComentarioService;
import com.matera.trainning.bookstore.service.exception.RecursoAlreadyExistsException;
import com.matera.trainning.bookstore.service.exception.RecursoNotFoundException;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ComentarioController.class)
public class ComentarioControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper jsonMapper;

	@MockBean
	private ComentarioService comentarioService;
	
	private AvaliacaoDTO dtoAvaliacao;

	private ComentarioDTO dtoComentario;
	
	@Before
	public void setUp() {		
		ProdutoDTO livroTheHobbit = new ProdutoDTO();
		livroTheHobbit.setCodigo("LIVRO23040");
		livroTheHobbit.setDescricao("Livro The Hobbit");
		livroTheHobbit.setPreco(new BigDecimal(57.63));
		livroTheHobbit.setDataCadastro(LocalDate.now());

		dtoComentario = new ComentarioDTO();
		dtoComentario.setCodigo("dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1");
		dtoComentario.setDescricao("Odiei, este é o pior livro do mundo!");
		dtoComentario.setUsuario("usuario.hater");
		dtoComentario.setDataHoraCriacao(LocalDateTime.now());
				
		dtoAvaliacao = new AvaliacaoDTO();
		dtoAvaliacao.setCodigo("dXN1YXJpby52321ASScsDE5MTcyNPhT2e=");
		dtoAvaliacao.setDescricao("Livro The Hobbit");
		dtoAvaliacao.setRating(3.0);
		dtoAvaliacao.setUsuario("usuario.hater");
	}
	
	@Test
	public void atualizaComentario() throws Exception {
		String jsonObject = jsonMapper.writeValueAsString(dtoComentario);
		mockMvc.perform(put("/v1/comentarios/{codComentario}", "dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.contentType(APPLICATION_JSON_UTF8)
				.content(jsonObject))
				.andExpect(status().isNoContent())
				.andExpect(content().string(isEmptyString()));
	}
	
	@Test
	public void atualizaComentarioInexistente() throws Exception {					
		doThrow(RecursoNotFoundException.class)
			.when(comentarioService).atualizarComentario(Mockito.anyString(), Mockito.any(ComentarioDTO.class));
		
		String jsonObject = jsonMapper.writeValueAsString(dtoComentario);
		mockMvc.perform(put("/v1/comentarios/{codComentario}", "dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.contentType(APPLICATION_JSON_UTF8)
				.content(jsonObject))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}
	
	@Test
	public void removeComentario() throws Exception {
		mockMvc.perform(delete("/v1/comentarios/{codComentario}", "dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isNoContent())
				.andExpect(content().string(isEmptyString()));
	}
	
	@Test
	public void removeComentarioInexistente() throws Exception {
		doThrow(RecursoNotFoundException.class)
			.when(comentarioService).removerComentario(Mockito.anyString());
		
		mockMvc.perform(delete("/v1/comentarios/{codComentario}", "dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}

	@Test
	public void buscaComentarioInexistentePeloCodigo() throws Exception {
		when(comentarioService.buscarComentarioDadoCodigo(Mockito.anyString()))
			.thenThrow(RecursoNotFoundException.class);
		
		mockMvc.perform(get("/v1/comentarios/{codComentario}", "dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}
	
	@Test
	public void buscaComentarioPeloCodigo() throws Exception {
		when(comentarioService.buscarComentarioDadoCodigo(Mockito.anyString()))
			.thenReturn(dtoComentario);
		
		String jsonObject = jsonMapper.writeValueAsString(dtoComentario);
		mockMvc.perform(get("/v1/comentarios/{codigo}", "dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(content().string(jsonObject))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}

	@Test
	public void listaComentarioPorUsuario() throws Exception {
		Page<ComentarioDTO> comentarios = new PageImpl<>(list(dtoComentario));

		when(comentarioService.listarComentariosDadoUsuario(Mockito.anyString(), Mockito.any(Pageable.class)))
			.thenReturn(comentarios);
		
		String jsonArray = jsonMapper.writeValueAsString(comentarios);
		mockMvc.perform(get("/v1/comentarios")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8)
				.param("usuario", "usuario.hater"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content", hasSize(1)))
				.andExpect(content().json(jsonArray))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void listaComentarioParaUsuarioInexistente() throws Exception {
		when(comentarioService.listarComentariosDadoUsuario(Mockito.anyString(), Mockito.any(Pageable.class)))
			.thenThrow(RecursoNotFoundException.class);
		
		mockMvc.perform(get("/v1/comentarios")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8)
				.param("usuario", "usuario.hater"))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}
	
	@Test
	public void listaAvaliacoesPeloComentario() throws Exception {
		Page<AvaliacaoDTO> comentarios = new PageImpl<>(list(dtoAvaliacao));

		when(comentarioService.listarAvaliacoesDadoCodigoComentario(Mockito.anyString(), Mockito.any(Pageable.class)))
			.thenReturn(comentarios);
		
		String jsonArray = jsonMapper.writeValueAsString(comentarios);
		mockMvc.perform(get("/v1/comentarios/{codComentario}/avaliacoes", "dXN1YXJpby52321ASScsDE5MTcyNPhT2e=")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content", hasSize(1)))
				.andExpect(content().json(jsonArray))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void listaAvaliacoesPeloComentarioInexistente() throws Exception {
		when(comentarioService.listarAvaliacoesDadoCodigoComentario(Mockito.anyString(), Mockito.any(Pageable.class)))
			.thenThrow(RecursoNotFoundException.class);
		
		mockMvc.perform(get("/v1/comentarios/{codComentario}/avaliacoes", "dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}
	
	@Test
	public void avaliaComentario() throws Exception {
		when(comentarioService.avaliarComentario(Mockito.anyString(), Mockito.any(AvaliacaoDTO.class)))
			.thenReturn(dtoAvaliacao);
		
		String jsonObject = jsonMapper.writeValueAsString(dtoAvaliacao);
		mockMvc.perform(post("/v1/comentarios/{codComentario}/avaliacoes", "dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.contentType(APPLICATION_JSON_UTF8)
				.content(jsonObject))
				.andExpect(status().isCreated())
				.andExpect(content().json(jsonObject))
				.andExpect(header().string("location", is("http://localhost/v1/avaliacoes/" + dtoAvaliacao.getCodigo())))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void avaliaComentarioDuplicado() throws Exception {
		when(comentarioService.avaliarComentario(Mockito.anyString(), Mockito.any(AvaliacaoDTO.class)))
			.thenThrow(new RecursoAlreadyExistsException("Avaliação já existente para o usuário", dtoAvaliacao.getCodigo(), "/v1/avaliacoes"));		
		
		String jsonObject = jsonMapper.writeValueAsString(dtoAvaliacao);
		mockMvc.perform(post("/v1/comentarios/{codComentario}/avaliacoes", "dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.contentType(APPLICATION_JSON_UTF8)
				.content(jsonObject))
				.andExpect(status().isConflict())
				.andExpect(header().string("location", is("http://localhost/v1/avaliacoes/" + dtoAvaliacao.getCodigo())))
				.andExpect(content().string(isEmptyString()));
	}
	
	@Test
	public void avaliaComentarioInexistente() throws Exception {
		when(comentarioService.avaliarComentario(Mockito.anyString(), Mockito.any(AvaliacaoDTO.class)))
			.thenThrow(RecursoNotFoundException.class);
		
		String jsonObject = jsonMapper.writeValueAsString(dtoAvaliacao);
		mockMvc.perform(post("/v1/comentarios/{codComentario}/avaliacoes", "dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.contentType(APPLICATION_JSON_UTF8)
				.content(jsonObject))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}

}