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
import com.matera.trainning.bookstore.controller.dto.ComentarioDTO;
import com.matera.trainning.bookstore.controller.mapper.AvaliacaoMapper;
import com.matera.trainning.bookstore.controller.mapper.ComentarioMapper;
import com.matera.trainning.bookstore.model.Avaliacao;
import com.matera.trainning.bookstore.model.Comentario;
import com.matera.trainning.bookstore.model.Produto;
import com.matera.trainning.bookstore.respository.ComentarioRepository;
import com.matera.trainning.bookstore.service.exception.RecursoAlreadyExistsException;
import com.matera.trainning.bookstore.service.exception.RecursoNotFoundException;

@RunWith(SpringRunner.class)
public class ComentarioServiceTest {

	@InjectMocks
	private ComentarioService comentarioService;
	
	@Mock
	private ComentarioMapper comentarioMapper;
	
	@Mock
	private AvaliacaoMapper avaliacaoMapper;
	
	@Mock
	private AvaliacaoService avaliacaoService;
	
	@Mock
	private ComentarioRepository comentarioRepository;		
	
	private Produto produto;
	private Comentario comentario;
	private Avaliacao avaliacao;

	@Before
	public void setUp() {		
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
	public void atualizarComentario() throws Exception {
		ModelMapper mapper = new ModelMapper();

		when(comentarioRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.of(comentario));		
		
		ComentarioDTO dtoEntrada = mapper.map(comentario, ComentarioDTO.class);
		dtoEntrada.setDescricao("Amei, melhor livro que já li.");
		
		comentarioService.atualizarComentario("dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1", dtoEntrada);
		
		assertThat(comentario.getDescricao()).isEqualTo(dtoEntrada.getDescricao());
	}
	
	@Test
	public void atualizarComentarioInexistente() throws Exception {		
		ModelMapper mapper = new ModelMapper();
		
		ComentarioDTO dtoEntrada = mapper.map(comentario, ComentarioDTO.class);
		dtoEntrada.setDescricao("Amei, melhor livro que já li.");

		when(comentarioRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.empty());
		
		try {
			comentarioService.atualizarComentario("dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1", dtoEntrada);
			fail();
		} catch (RecursoNotFoundException ex) {
			assertThat(comentario.getDescricao()).isNotEqualTo(dtoEntrada.getDescricao());
			assertEquals("Comentário " + comentario.getCodigo() + " inexistente", ex.getMessage());
		}
	}
	
	@Test
	public void removerComentario() throws Exception {
		when(comentarioRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.of(comentario));
		
		comentarioService.removerComentario("dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1");		
	}
	
	@Test
	public void removerComentarioInexistente() throws Exception {
		when(comentarioRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.empty());
		
		try {
			comentarioService.removerComentario("dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1");
			fail();
		} catch (RecursoNotFoundException ex) {
			assertEquals("Comentário " + comentario.getCodigo() + " inexistente", ex.getMessage());
		}
	}
	
	@Test
	public void buscarComentarioDadoCodigo() throws Exception {
		ModelMapper mapper = new ModelMapper();

		when(comentarioRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.of(comentario));

		ComentarioDTO dtoComentario = mapper.map(comentario, ComentarioDTO.class);
		when(comentarioMapper.toDto(Mockito.any(Comentario.class)))
			.thenReturn(dtoComentario);		
		
		ComentarioDTO dtoSaida = comentarioService.buscarComentarioDadoCodigo("dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1");
				
		assertThat(dtoSaida).isNotNull().isEqualTo(dtoComentario);
	}
	
	@Test
	public void buscarComentarioDadoCodigoInexistente() {
		when(comentarioRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.empty());
		
		try {
			comentarioService.buscarComentarioDadoCodigo("dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1");
			fail();
		} catch (RecursoNotFoundException ex) {
			assertEquals("Comentário " + comentario.getCodigo() + " inexistente", ex.getMessage());
		}
	}
	
	@Test
	public void listarComentariosDadoProduto() throws Exception {
		ModelMapper mapper = new ModelMapper();

		avaliacao.setProduto(produto);
		
		Page<Comentario> pgComentarios = new PageImpl<>(list(comentario));
		when(comentarioRepository.findAllByProduto(Mockito.any(Produto.class), Mockito.any(Pageable.class)))
			.thenReturn(pgComentarios);
		
		ComentarioDTO dtoComentario = mapper.map(comentario, ComentarioDTO.class);
		
		when(comentarioMapper.toDto(Mockito.any(Comentario.class)))
			.thenReturn(dtoComentario);
		
		List<ComentarioDTO> comentarios = comentarioService.listarComentariosDadoProduto(produto, PageRequest.of(0, 1)).getContent();
		
		assertThat(comentarios).isNotEmpty().hasSize(1).contains(dtoComentario);		
	}
	
	@Test
	public void listarComentariosDadoProdutoInexistente() throws Exception {				
		Page<Comentario> pgComentarios = new PageImpl<>(list());
		
		when(comentarioRepository.findAllByProduto(Mockito.any(Produto.class), Mockito.any(Pageable.class)))
			.thenReturn(pgComentarios);
						
		List<ComentarioDTO> comentarios = comentarioService.listarComentariosDadoProduto(produto, PageRequest.of(0, 1)).getContent();
		
		assertThat(comentarios).hasSize(0);	
	}
	
	@Test
	public void listaComentariosEmBasePopulada() {
		ModelMapper mapper = new ModelMapper();

		avaliacao.setProduto(produto);
		
		Page<Comentario> pgComentarios = new PageImpl<>(list(comentario));
		when(comentarioRepository.findAll(Mockito.any(Pageable.class)))
			.thenReturn(pgComentarios);
		
		ComentarioDTO dtoComentario = mapper.map(comentario, ComentarioDTO.class);
		
		when(comentarioMapper.toDto(Mockito.any(Comentario.class)))
			.thenReturn(dtoComentario);
		
		List<ComentarioDTO> comentarios = comentarioService.listarComentarios(PageRequest.of(0, 1)).getContent();
		
		assertThat(comentarios).isNotEmpty().hasSize(1).contains(dtoComentario);
	}
	
	@Test
	public void listaComentariosEmBaseVazia() {
		Page<Comentario> pgComentarios = new PageImpl<>(list());
		
		when(comentarioRepository.findAll(Mockito.any(Pageable.class)))
			.thenReturn(pgComentarios);

		List<ComentarioDTO> comentarios = comentarioService.listarComentarios(PageRequest.of(0, 1)).getContent();
		
		assertThat(comentarios).hasSize(0);
	}
	
	@Test
	public void listarComentariosDadoUsuario() throws Exception {
		ModelMapper mapper = new ModelMapper();

		Page<Comentario> pgComentarios = new PageImpl<>(list(comentario));
		when(comentarioRepository.findAllByUsuario(Mockito.anyString(), Mockito.any(Pageable.class)))
			.thenReturn(pgComentarios);
		
		ComentarioDTO dtoComentario = mapper.map(comentario, ComentarioDTO.class);	
		
		when(comentarioMapper.toDto(Mockito.any(Comentario.class)))
			.thenReturn(dtoComentario);
		
		List<ComentarioDTO> comentarios = comentarioService.listarComentariosDadoUsuario("usuario.teste", PageRequest.of(0, 1)).getContent();
		
		assertThat(comentarios).isNotEmpty().hasSize(1).contains(dtoComentario);		
	}
	
	@Test
	public void listarComentariosDadoUsuarioInexistente() throws Exception {	
		Page<Comentario> pgComentarios = new PageImpl<>(list());
		
		when(comentarioRepository.findAllByUsuario(Mockito.anyString(), Mockito.any(Pageable.class)))
			.thenReturn(pgComentarios);		
		
		try {
			comentarioService.listarComentariosDadoUsuario("usuario.teste", PageRequest.of(0, 1)).getContent();
			fail();
		} catch (RecursoNotFoundException ex) {
			assertEquals("Usuário " + comentario.getUsuario() + " inexistente", ex.getMessage());
		}								
	}
	
	@Test
	public void listarComentariosComRatingMaiorQueParam() throws Exception {
		comentario.setRating(2.5);

		Page<Comentario> pgComentarios = new PageImpl<>(list(comentario));
		when(comentarioRepository.findAllByRatingGreaterThanParam(Mockito.anyDouble(), Mockito.any(Pageable.class)))
			.thenReturn(pgComentarios);

		ModelMapper mapper = new ModelMapper();
		ComentarioDTO dtoComentario = mapper.map(comentario, ComentarioDTO.class);
		
		when(comentarioMapper.toDto(Mockito.any(Comentario.class)))
			.thenReturn(dtoComentario);

		List<ComentarioDTO> comentarios = comentarioService.listarComentariosComRatingMaiorQueParam(2.0, PageRequest.of(0, 1)).getContent();

		assertThat(comentarios).isNotEmpty().hasSize(1).contains(dtoComentario);
	}
	
	@Test
	public void listarComentariosComRatingMaiorQueParamVazio() throws Exception {
		Page<Comentario> pgComentarios = new PageImpl<>(list());
		
		when(comentarioRepository.findAllByRatingGreaterThanParam(Mockito.anyDouble(), Mockito.any(Pageable.class)))
			.thenReturn(pgComentarios);

		List<ComentarioDTO> comentarios = comentarioService.listarComentariosComRatingMaiorQueParam(2.0, PageRequest.of(0, 1)).getContent();

		assertThat(comentarios).hasSize(0);
	}
	
	@Test
	public void listarAvaliacoesDadoCodigoComentario() throws Exception {
		ModelMapper mapper = new ModelMapper();

		when(comentarioRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.of(comentario));
		
		AvaliacaoDTO dtoAvaliacao = mapper.map(avaliacao, AvaliacaoDTO.class);
		Page<AvaliacaoDTO> pgAvaliacoes = new PageImpl<>(list(dtoAvaliacao));
		
		when(avaliacaoService.listarAvaliacoesDadoComentario(Mockito.any(Comentario.class), Mockito.any(Pageable.class)))
			.thenReturn(pgAvaliacoes);
			
		List<AvaliacaoDTO> avaliacoes = comentarioService.listarAvaliacoesDadoCodigoComentario("dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1", PageRequest.of(0, 1)).getContent();
		
		assertThat(avaliacoes).isNotEmpty().hasSize(1).contains(dtoAvaliacao);		
	}
	
	@Test
	public void listarAvaliacoesDadoCodigoComentarioInexistente() throws Exception {
		when(comentarioRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.empty());

		try {
			comentarioService.listarAvaliacoesDadoCodigoComentario("dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1", PageRequest.of(0, 1)).getContent();			
			fail();
		} catch (RecursoNotFoundException ex) {
			assertEquals("Comentário " + comentario.getCodigo() + " inexistente", ex.getMessage());
		}
	}
	
	@Test
	public void avaliarComentario() throws Exception {
		ModelMapper mapper = new ModelMapper();

		when(comentarioRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.of(comentario));
		
		AvaliacaoDTO dtoEntrada = mapper.map(avaliacao, AvaliacaoDTO.class);
		
		dtoEntrada.setDescricao(comentario.getDescricao());

		when(avaliacaoMapper.toEntity(Mockito.any(AvaliacaoDTO.class)))
			.thenReturn(avaliacao);
				
		when(avaliacaoMapper.toDto(avaliacao))
			.thenReturn(dtoEntrada);
		
		AvaliacaoDTO dtoSaida = comentarioService.avaliarComentario("dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1", dtoEntrada);
		
		assertThat(dtoSaida).isNotNull();
		assertThat(dtoSaida.getDescricao()).isNotNull().isEqualTo(comentario.getDescricao());
		assertThat(avaliacao.getComentario()).isNotNull().isEqualTo(comentario);
		assertThat(comentario.getAvaliacoes()).isNotEmpty().contains(avaliacao);
	}
	
	@Test
	public void avaliarComentarioInexistente() throws Exception {		
		when(comentarioRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.empty());		
		
		ModelMapper mapper = new ModelMapper();
		AvaliacaoDTO dtoEntrada = mapper.map(avaliacao, AvaliacaoDTO.class);

		try {
			comentarioService.avaliarComentario("dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1", dtoEntrada);
			fail();
		} catch (RecursoNotFoundException ex) {
			assertEquals("Comentário " + comentario.getCodigo() + " inexistente", ex.getMessage());
		}								
	}
	
	@Test
	public void avaliarComentarioDuplicado() throws Exception {
		comentario.addAvaliacao(avaliacao);
		
		when(comentarioRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.of(comentario));

		ModelMapper mapper = new ModelMapper();
		AvaliacaoDTO dtoEntrada = mapper.map(avaliacao, AvaliacaoDTO.class);

		try {
			comentarioService.avaliarComentario("dXN1YXJpby5oYXRlcjMwMDEyMDE5MTcyNDI1", dtoEntrada);
			fail();
		} catch (RecursoAlreadyExistsException ex) {
			assertEquals("Avaliação já existente para o usuário " + dtoEntrada.getUsuario(), ex.getMessage());
		}
	}

}
