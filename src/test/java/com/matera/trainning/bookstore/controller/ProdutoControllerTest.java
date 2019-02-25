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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matera.trainning.bookstore.controller.dto.AvaliacaoDTO;
import com.matera.trainning.bookstore.controller.dto.ComentarioDTO;
import com.matera.trainning.bookstore.controller.dto.HistoricoDePrecoDTO;
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

	private ProdutoDTO dtoProduto;
	
	private ComentarioDTO dtoComentario; 
	
	private HistoricoDePrecoDTO dtoItemHistPreco;
	
	private AvaliacaoDTO dtoAvaliacao;

	@Before
	public void setUp() {				
		dtoProduto = new ProdutoDTO();
		dtoProduto.setCodigo("LIVRO23040");
		dtoProduto.setDescricao("Livro The Hobbit");
		dtoProduto.setPreco(new BigDecimal(57.63));
		dtoProduto.setDataCadastro(LocalDate.now());
		
		dtoComentario = new ComentarioDTO();
		dtoComentario.setCodigo("dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1");
		dtoComentario.setDescricao("Odiei, este é o pior livro do mundo!");
		dtoComentario.setUsuario("usuario.hater");
		dtoComentario.setDataHoraCriacao(LocalDateTime.now());
		
		dtoItemHistPreco = new HistoricoDePrecoDTO();
		dtoItemHistPreco.setPreco(new BigDecimal(57.63));
		dtoItemHistPreco.setDescricao(dtoProduto.getDescricao());
		dtoItemHistPreco.setDataHoraAlteracao(LocalDateTime.now());
		
		dtoAvaliacao = new AvaliacaoDTO();
		dtoAvaliacao.setCodigo("dXN1YXJpby52321ASScsDE5MTcyNPhT2e=");
		dtoAvaliacao.setDescricao("Livro The Hobbit");
		dtoAvaliacao.setRating(3.0);
		dtoAvaliacao.setUsuario("usuario.hater");
	}

	@Test
	public void listaProdutosEmBasePopulada() throws Exception {
		Page<ProdutoDTO> produtos = new PageImpl<>(list(dtoProduto));
		
		when(produtoService.listarProdutos(Mockito.any(Pageable.class)))
			.thenReturn(produtos);
		
		String jsonArray = jsonMapper.writeValueAsString(produtos);
		mockMvc.perform(get("/v1/produtos")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
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
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content", hasSize(0)))
				.andExpect(content().json(jsonArray))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}

	@Test
	public void insereProduto() throws Exception {		
		when(produtoService.inserirProduto(Mockito.any(ProdutoDTO.class)))
			.thenReturn(dtoProduto);	
		
		String jsonObject = jsonMapper.writeValueAsString(dtoProduto);
		mockMvc.perform(post("/v1/produtos")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.contentType(APPLICATION_JSON_UTF8)
				.content(jsonObject))
				.andExpect(status().isCreated())
				.andExpect(content().json(jsonObject))
				.andExpect(header().string("location", is("http://localhost/v1/produtos/" + dtoProduto.getCodigo())))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void insereProdutoDuplicado() throws Exception {
		when(produtoService.inserirProduto(Mockito.any(ProdutoDTO.class)))
			.thenThrow(new RecursoAlreadyExistsException("Produto já existente", dtoProduto.getCodigo(), "/v1/produtos"));		

		String jsonObject = jsonMapper.writeValueAsString(dtoProduto);
		mockMvc.perform(post("/v1/produtos")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.contentType(APPLICATION_JSON_UTF8)
				.content(jsonObject))
				.andExpect(status().isConflict())
				.andExpect(header().string("location", is("http://localhost/v1/produtos/" + dtoProduto.getCodigo())))
				.andExpect(content().string(isEmptyString()));
	}
	
	@Test
	public void atualizaProduto() throws Exception {
		String jsonObject = jsonMapper.writeValueAsString(dtoProduto);
		mockMvc.perform(put("/v1/produtos/{codProduto}", "LIVRO23040")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.contentType(APPLICATION_JSON_UTF8)
				.content(jsonObject))
				.andExpect(status().isNoContent())
				.andExpect(content().string(isEmptyString()));
	}
	
	@Test
	public void atualizaProdutoInexistente() throws Exception {
		doThrow(RecursoNotFoundException.class)
			.when(produtoService).atualizarProduto(Mockito.anyString(), Mockito.any(ProdutoDTO.class));
		
		String jsonObject = jsonMapper.writeValueAsString(dtoProduto);
		mockMvc.perform(put("/v1/produtos/{codProduto}", "LIVRO23040")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.contentType(APPLICATION_JSON_UTF8)
				.content(jsonObject))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}
	
	@Test
	public void removeProduto() throws Exception {
		mockMvc.perform(delete("/v1/produtos/{codProduto}", "LIVRO23040")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isNoContent())
				.andExpect(content().string(isEmptyString()));
	}
	
	@Test
	public void removeProdutoInexistente() throws Exception {
		doThrow(RecursoNotFoundException.class).when(produtoService)
			.removerProduto(Mockito.anyString());
		
		mockMvc.perform(delete("/v1/produtos/{codProduto}", "LIVRO23040")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}

	@Test
	public void buscaProdutoPeloCodigo() throws Exception {
		when(produtoService.buscarProdutoDadoCodigo(Mockito.anyString()))
			.thenReturn(dtoProduto);

		String jsonObject = jsonMapper.writeValueAsString(dtoProduto);
		mockMvc.perform(get("/v1/produtos/{codProduto}", "LIVRO23040")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
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
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}

	@Test
	public void buscaProdutoPelaDescricao() throws Exception {
		Page<ProdutoDTO> produtos = new PageImpl<>(list(dtoProduto));

		when(produtoService.listarProdutosDadoDescricao(Mockito.anyString(), Mockito.any(Pageable.class)))
			.thenReturn(produtos);

		String jsonArray = jsonMapper.writeValueAsString(produtos);
		mockMvc.perform(get("/v1/produtos")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
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

		when(produtoService.listarProdutosDadoDescricao(Mockito.anyString(), Mockito.any(Pageable.class)))
			.thenReturn(produtos);

		String jsonArray = jsonMapper.writeValueAsString(produtos);
		mockMvc.perform(get("/v1/produtos")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8)
				.param("descricao", "LiVrO"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content", hasSize(0)))
				.andExpect(content().json(jsonArray))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));		
	}
	
	@Test
	public void listaComentariosPeloProduto() throws Exception {
		Page<ComentarioDTO> comentarios = new PageImpl<>(list(dtoComentario));

		when(produtoService.listarComentariosDadoCodigoProduto(Mockito.anyString(), Mockito.any(Pageable.class)))
			.thenReturn(comentarios);
		
		String jsonArray = jsonMapper.writeValueAsString(comentarios);
		mockMvc.perform(get("/v1/produtos/{codProduto}/comentarios", "LIVRO23040")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
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
	
		mockMvc.perform(get("/v1/produtos/{codProduto}/comentarios", "LIVRO23040")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}
	
	@Test
	public void comentaProduto() throws Exception {
		when(produtoService.comentarProduto(Mockito.anyString(), Mockito.any(ComentarioDTO.class)))
			.thenReturn(dtoComentario);
		
		String jsonObject = jsonMapper.writeValueAsString(dtoComentario);
		mockMvc.perform(post("/v1/produtos/{codProduto}/comentarios", "LIVRO23040")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.contentType(APPLICATION_JSON_UTF8)
				.content(jsonObject))
				.andExpect(status().isCreated())
				.andExpect(content().json(jsonObject))
				.andExpect(header().string("location", is("http://localhost/v1/comentarios/" + dtoComentario.getCodigo())))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void comentaProdutoInexistente() throws Exception {
		when(produtoService.comentarProduto(Mockito.anyString(), Mockito.any(ComentarioDTO.class)))
			.thenThrow(RecursoNotFoundException.class);
		
		String jsonObject = jsonMapper.writeValueAsString(dtoComentario);
		mockMvc.perform(post("/v1/produtos/{codProduto}/comentarios", "LIVRO23040")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.contentType(APPLICATION_JSON_UTF8)
				.content(jsonObject))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}
	
	@Test
	public void listaHistoricoDePrecosPeloProduto() throws Exception {
		Page<HistoricoDePrecoDTO> historicoDePrecos = new PageImpl<>(list(dtoItemHistPreco));

		when(produtoService.listarHistoricoDePrecosDadoProduto(Mockito.anyString(), Mockito.any(Pageable.class)))
			.thenReturn(historicoDePrecos);
		
		String jsonArray = jsonMapper.writeValueAsString(historicoDePrecos);
		mockMvc.perform(get("/v1/produtos/{codProduto}/historico-precos", "LIVRO23040")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content", hasSize(1)))
				.andExpect(content().json(jsonArray))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void listaHistoricoDePrecosPeloProdutoInexistente() throws Exception {
		when(produtoService.listarHistoricoDePrecosDadoProduto(Mockito.anyString(), Mockito.any(Pageable.class)))
			.thenThrow(RecursoNotFoundException.class);
		
		mockMvc.perform(get("/v1/comentarios/{codComentario}/avaliacoes", "LIVRO23040")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}
	
	@Test
	public void listaHistoricoDePrecosNoPeriodoPeloProduto() throws Exception {
		Page<HistoricoDePrecoDTO> historicoDePrecos = new PageImpl<>(list(dtoItemHistPreco));

		when(produtoService.listarHistoricoDePrecosNoPeriodoDadoProduto(Mockito.anyString(), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class), Mockito.any(Pageable.class)))
			.thenReturn(historicoDePrecos);

		String jsonArray = jsonMapper.writeValueAsString(historicoDePrecos);
		mockMvc.perform(get("/v1/produtos/{codProduto}/historico-precos", "LIVRO23040")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8)
				.param("dtInicio", LocalDate.now().toString())
				.param("dtFim", LocalDate.now().toString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content", hasSize(1)))
				.andExpect(content().json(jsonArray))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void listaHistoricoDePrecosNoPeriodoPeloProdutoInexistente() throws Exception {
		when(produtoService.listarHistoricoDePrecosNoPeriodoDadoProduto(Mockito.anyString(), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class), Mockito.any(Pageable.class)))
			.thenThrow(RecursoNotFoundException.class);

		mockMvc.perform(get("/v1/produtos/{codProduto}/historico-precos", "LIVRO23040")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8)
				.param("dtInicio", LocalDate.now().toString())
				.param("dtFim", LocalDate.now().toString()))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}
	
	@Test
	public void buscaHistoricoDePrecoMaximoPeloProduto() throws Exception {
		when(produtoService.buscarPrecoMaximoDadoCodigoProduto(Mockito.anyString()))
			.thenReturn(dtoItemHistPreco);

		String jsonObject = jsonMapper.writeValueAsString(dtoItemHistPreco);
		mockMvc.perform(get("/v1/produtos/{codProduto}/historico-precos/max", "LIVRO23040")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonObject))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void buscaHistoricoDePrecoMaximoPeloProdutoInexistente() throws Exception {
		when(produtoService.buscarPrecoMaximoDadoCodigoProduto(Mockito.anyString()))
			.thenThrow(RecursoNotFoundException.class);

		mockMvc.perform(get("/v1/produtos/{codProduto}/historico-precos/max", "LIVRO23040")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8)
				.param("preco", "max"))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}
	
	@Test
	public void buscaHistoricoDePrecoMinimoPeloProduto() throws Exception {
		when(produtoService.buscarPrecoMinimoDadoCodigoProduto(Mockito.anyString()))
			.thenReturn(dtoItemHistPreco);

		String jsonObject = jsonMapper.writeValueAsString(dtoItemHistPreco);
		mockMvc.perform(get("/v1/produtos/{codProduto}/historico-precos/min", "LIVRO23040")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonObject))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}
	
	@Test
	@WithMockUser(username = "user", password = "password")
	public void buscaHistoricoDePrecoMinimoPeloProdutoInexistente() throws Exception {
		when(produtoService.buscarPrecoMinimoDadoCodigoProduto(Mockito.anyString()))
			.thenThrow(RecursoNotFoundException.class);

		mockMvc.perform(get("/v1/produtos/{codProduto}/historico-precos/min", "LIVRO23040")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}
	
	@Test
	public void buscaHistoricoDePrecoMaximoNoPeriodoPeloProduto() throws Exception {
		when(produtoService.buscarPrecoMaximoNoIntervaloDadoCodigoProdutoV1(Mockito.anyString(), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)))
			.thenReturn(dtoItemHistPreco);

		String jsonObject = jsonMapper.writeValueAsString(dtoItemHistPreco);
		mockMvc.perform(get("/v1/produtos/{codProduto}/historico-precos", "LIVRO23040")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8)
				.param("dtInicio", LocalDate.now().toString())
				.param("dtFim", LocalDate.now().toString())
				.param("preco", "max"))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonObject))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void buscaHistoricoDePrecoMinimoNoPeriodoPeloProduto() throws Exception {
		when(produtoService.buscarPrecoMinimoNoIntervaloDadoCodigoProdutoV1(Mockito.anyString(), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)))
			.thenReturn(dtoItemHistPreco);

		String jsonObject = jsonMapper.writeValueAsString(dtoItemHistPreco);
		mockMvc.perform(get("/v1/produtos/{codProduto}/historico-precos", "LIVRO23040")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8)
				.param("dtInicio", LocalDate.now().toString())
				.param("dtFim", LocalDate.now().toString())
				.param("preco", "min"))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonObject))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void listaAvaliacoesPeloProduto() throws Exception {
		Page<AvaliacaoDTO> avaliacoes = new PageImpl<>(list(dtoAvaliacao));

		when(produtoService.listarAvaliacoesDadoCodigoDoProduto(Mockito.anyString(), Mockito.any(Pageable.class)))
			.thenReturn(avaliacoes);
		
		String jsonArray = jsonMapper.writeValueAsString(avaliacoes);
		mockMvc.perform(get("/v1/produtos/{codProduto}/avaliacoes", "LIVRO23040")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content", hasSize(1)))
				.andExpect(content().json(jsonArray))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void listaAvaliacoesPeloComentarioInexistente() throws Exception {
		when(produtoService.listarAvaliacoesDadoCodigoDoProduto(Mockito.anyString(), Mockito.any(Pageable.class)))
			.thenThrow(RecursoNotFoundException.class);
		
		mockMvc.perform(get("/v1/produtos/{codProduto}/avaliacoes", "LIVRO23040=")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}
	
	@Test
	public void avaliaProduto() throws Exception {
		when(produtoService.avaliarProduto(Mockito.anyString(), Mockito.any(AvaliacaoDTO.class)))
			.thenReturn(dtoAvaliacao);
		
		String jsonObject = jsonMapper.writeValueAsString(dtoAvaliacao);
		mockMvc.perform(post("/v1/produtos/{codProduto}/avaliacoes", "LIVRO23040")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.contentType(APPLICATION_JSON_UTF8)
				.content(jsonObject))
				.andExpect(status().isCreated())
				.andExpect(content().json(jsonObject))
				.andExpect(header().string("location", is("http://localhost/v1/avaliacoes/" + dtoAvaliacao.getCodigo())))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void avaliaProdutoInexistente() throws Exception {
		when(produtoService.avaliarProduto(Mockito.anyString(), Mockito.any(AvaliacaoDTO.class)))
			.thenThrow(RecursoNotFoundException.class);
		
		String jsonObject = jsonMapper.writeValueAsString(dtoAvaliacao);
		mockMvc.perform(post("/v1/produtos/{codProduto}/avaliacoes", "LIVRO23040")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.contentType(APPLICATION_JSON_UTF8)
				.content(jsonObject))
				.andExpect(status().isNotFound())
				.andExpect(content().string(isEmptyString()));
	}
	
}
