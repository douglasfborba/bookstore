package com.matera.trainning.bookstore.controller;

import static org.assertj.core.util.Lists.list;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.Base64Utils.encodeToString;

import java.math.BigDecimal;
import java.time.LocalDate;

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
import com.matera.trainning.bookstore.controller.dto.ProdutoDTO;
import com.matera.trainning.bookstore.service.AvaliacaoService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = AvaliacaoController.class)
public class AvaliacaoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper jsonMapper;

	@MockBean
	private AvaliacaoService service;

	private AvaliacaoDTO avaliacao;

	@Before
	public void setUp() {
		ProdutoDTO livroTheHobbit = new ProdutoDTO();
		livroTheHobbit.setCodigo("LIVRO23040");
		livroTheHobbit.setDescricao("Livro The Hobbit");
		livroTheHobbit.setPreco(new BigDecimal(57.63));
		livroTheHobbit.setDataCadastro(LocalDate.now());

		avaliacao = new AvaliacaoDTO();
		avaliacao.setCodigo("dXN1YXJpby52321ASScsDE5MTcyNPhT2e=");
		avaliacao.setDescricao("Livro The Hobbit");
		avaliacao.setRating(3.0);
		avaliacao.setUsuario("usuario.hater");
	}
	
	@Test
	public void listaAvaliacoesPorUsuario() throws Exception {
		Page<AvaliacaoDTO> avaliacoes = new PageImpl<>(list(avaliacao));

		when(service.listarAvaliacoesDadoUsuario(Mockito.anyString(), Mockito.any(Pageable.class)))
			.thenReturn(avaliacoes);
		
		String jsonArray = jsonMapper.writeValueAsString(avaliacoes);
		mockMvc.perform(get("/v1/avaliacoes")
				.header(AUTHORIZATION, "Basic " + encodeToString("user:password".getBytes()))
				.accept(APPLICATION_JSON_UTF8)
				.param("usuario", "usuario.hater"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content", hasSize(1)))
				.andExpect(content().json(jsonArray))
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));
	}

}
