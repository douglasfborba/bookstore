package com.matera.trainning.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.matera.trainning.bookstore.controller.dto.AvaliacaoDTO;
import com.matera.trainning.bookstore.model.Avaliacao;
import com.matera.trainning.bookstore.model.Comentario;
import com.matera.trainning.bookstore.model.Produto;
import com.matera.trainning.bookstore.respository.AvaliacaoRepository;
import com.matera.trainning.bookstore.service.exception.RecursoNotFoundException;

@RunWith(SpringRunner.class)
public class AvaliacaoServiceTest {
	
	@InjectMocks
	private AvaliacaoService avaliacaoService;
	
	@Mock
	private ModelMapper modelMapper;
	
	@Mock
	private AvaliacaoRepository avaliacaoRepository;
	
	private Produto produto;
	private Comentario comentario;
	private Avaliacao avaliacao;

	@Before
	public void setUp() {
		avaliacaoService.configuraMapper();
		
		produto = new Produto();
		produto.setCodigo("LIVRO23040");
		produto.setDescricao("Livro The Hobbit");
		produto.setPreco(new BigDecimal(57.63));
		produto.setDataCadastro(LocalDate.now());

		comentario = new Comentario();
		comentario.setCodigo("dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1");
		comentario.setProduto(produto);
		comentario.setUsuario("usuario.teste");
		comentario.setDescricao("Odiei, este é o pior livro do mundo!");
		comentario.setDataHoraCriacao(LocalDateTime.now());
		
		avaliacao = new Avaliacao();
		avaliacao.setCodigo("1YXJpby5oYXRlco124qWErTEyMDE5MTckQtz");
		avaliacao.setRating(3.5);
		avaliacao.setUsuario("usuario.teste");
	}

	@Test
	public void buscarAvaliacaoDadoCodigo() {
		ModelMapper mapper = criaAndConfiguraMapper(); 
		
		avaliacao.setProduto(produto);
			
		when(avaliacaoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.of(avaliacao));
		
		AvaliacaoDTO dtoAvaliacao = mapper.map(avaliacao, AvaliacaoDTO.class);
		when(modelMapper.map(Mockito.any(Object.class), Mockito.any()))
			.thenReturn(dtoAvaliacao);
	
		AvaliacaoDTO dtoSaida = avaliacaoService.buscarAvaliacaoDadoCodigo("1YXJpby5oYXRlco124qWErTEyMDE5MTckQtz");

		assertThat(dtoSaida).isNotNull().isEqualTo(dtoAvaliacao);
	}
	
	@Test
	public void buscarAvaliacaoDadoCodigoInexistente() {				
		when(avaliacaoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.empty());
		
		try {
			avaliacaoService.buscarAvaliacaoDadoCodigo("1YXJpby5oYXRlco124qWErTEyMDE5MTckQtz");
			fail();
		} catch (RecursoNotFoundException ex) {
			assertEquals("Avaliação " + avaliacao.getCodigo() + " inexistente", ex.getMessage());
		}
	}

	@Test
	public void listarAvaliacoesDadoProduto() throws Exception {
		ModelMapper mapper = criaAndConfiguraMapper(); 
		
		avaliacao.setProduto(produto);
		
		Page<Avaliacao> pgAvaliacoes = new PageImpl<>(list(avaliacao));
		when(avaliacaoRepository.findAllByProduto(Mockito.any(Produto.class), Mockito.any(Pageable.class)))
			.thenReturn(pgAvaliacoes);
		
		AvaliacaoDTO dtoAvaliacao = mapper.map(avaliacao, AvaliacaoDTO.class);	
		when(modelMapper.map(Mockito.any(Object.class), Mockito.any()))
			.thenReturn(dtoAvaliacao);
		
		List<AvaliacaoDTO> avaliacoes = avaliacaoService.listarAvaliacoesDadoProduto(produto, PageRequest.of(0, 1)).getContent();
		
		assertThat(avaliacoes).isNotEmpty().hasSize(1).contains(dtoAvaliacao);		
	}
	
	@Test
	public void listarAvaliacoesDadoProdutoInexistente() throws Exception {				
		Page<Avaliacao> pgAvaliacoes = new PageImpl<>(list());
		
		when(avaliacaoRepository.findAllByProduto(Mockito.any(Produto.class), Mockito.any(Pageable.class)))
			.thenReturn(pgAvaliacoes);
						
		List<AvaliacaoDTO> avaliacoes = avaliacaoService.listarAvaliacoesDadoProduto(produto, PageRequest.of(0, 1)).getContent();
		
		assertThat(avaliacoes).hasSize(0);	
	}
	
	@Test
	public void listarAvaliacoesDadoComentario() throws Exception {
		ModelMapper mapper = criaAndConfiguraMapper(); 
		
		avaliacao.setComentario(comentario);
		
		Page<Avaliacao> pgAvaliacoes = new PageImpl<>(list(avaliacao));
		when(avaliacaoRepository.findAllByComentario(Mockito.any(Comentario.class), Mockito.any(Pageable.class)))
			.thenReturn(pgAvaliacoes);
		
		AvaliacaoDTO dtoAvaliacao = mapper.map(avaliacao, AvaliacaoDTO.class);	
		when(modelMapper.map(Mockito.any(Object.class), Mockito.any()))
			.thenReturn(dtoAvaliacao);
		
		List<AvaliacaoDTO> avaliacoes = avaliacaoService.listarAvaliacoesDadoComentario(comentario, PageRequest.of(0, 1)).getContent();
		
		assertThat(avaliacoes).isNotEmpty().hasSize(1).contains(dtoAvaliacao);		
	}
	
	@Test
	public void listarAvaliacoesDadoComentarioInexistente() throws Exception {				
		Page<Avaliacao> pgAvaliacoes = new PageImpl<>(list());
		
		when(avaliacaoRepository.findAllByComentario(Mockito.any(Comentario.class), Mockito.any(Pageable.class)))
			.thenReturn(pgAvaliacoes);
						
		List<AvaliacaoDTO> avaliacoes = avaliacaoService.listarAvaliacoesDadoComentario(comentario, PageRequest.of(0, 1)).getContent();
		
		assertThat(avaliacoes).hasSize(0);	
	}
	
	@Test
	public void listarAvaliacoesDadoUsuario() throws Exception {
		ModelMapper mapper = criaAndConfiguraMapper(); 
				
		Page<Avaliacao> pgAvaliacoes = new PageImpl<>(list(avaliacao));
		when(avaliacaoRepository.findAllByUsuario(Mockito.anyString(), Mockito.any(Pageable.class)))
			.thenReturn(pgAvaliacoes);
		
		AvaliacaoDTO dtoAvaliacao = mapper.map(avaliacao, AvaliacaoDTO.class);	
		when(modelMapper.map(Mockito.any(Object.class), Mockito.any()))
			.thenReturn(dtoAvaliacao);
		
		List<AvaliacaoDTO> avaliacoes = avaliacaoService.listarAvaliacoesDadoUsuario("usuario.teste", PageRequest.of(0, 1)).getContent();
		
		assertThat(avaliacoes).isNotEmpty().hasSize(1).contains(dtoAvaliacao);		
	}
	
	@Test
	public void listarAvaliacoesDadoUsuarioInexistente() throws Exception {				
		Page<Avaliacao> pgAvaliacoes = new PageImpl<>(list());
		
		when(avaliacaoRepository.findAllByUsuario(Mockito.anyString(), Mockito.any(Pageable.class)))
			.thenReturn(pgAvaliacoes);
						
		List<AvaliacaoDTO> avaliacoes = avaliacaoService.listarAvaliacoesDadoUsuario("usuario.teste", PageRequest.of(0, 1)).getContent();
		
		assertThat(avaliacoes).hasSize(0);	
	}
	
	private ModelMapper criaAndConfiguraMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.addConverter(AvaliacaoDTO.getConverter());
		return mapper;
	}

}
