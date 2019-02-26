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

import com.matera.trainning.bookstore.controller.dto.ProdutoDTO;
import com.matera.trainning.bookstore.controller.mapper.ProdutoMapper;
import com.matera.trainning.bookstore.model.Avaliacao;
import com.matera.trainning.bookstore.model.Comentario;
import com.matera.trainning.bookstore.model.Produto;
import com.matera.trainning.bookstore.respository.ProdutoRepository;
import com.matera.trainning.bookstore.service.exception.RecursoAlreadyExistsException;
import com.matera.trainning.bookstore.service.exception.RecursoNotFoundException;

@RunWith(SpringRunner.class)
public class ProdutoServiceTest {

	@InjectMocks
	private ProdutoService produtoService;
	
	@Mock
	private ProdutoMapper produtoMapper;
	
	@Mock
	private ProdutoRepository produtoRepository;
	
	@Mock
	private AvaliacaoService avaliacaoService;
	
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
	public void inserirProduto() {		
		ModelMapper mapper = new ModelMapper();

		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.empty())
			.thenReturn(Optional.of(produto));
		
		ProdutoDTO dtoEntrada = mapper.map(produto, ProdutoDTO.class);
		when(produtoMapper.toDto(Mockito.any(Produto.class)))
			.thenReturn(dtoEntrada);
		
		when(produtoMapper.toEntity(Mockito.any(ProdutoDTO.class)))
			.thenReturn(produto);
		
		when(produtoRepository.save(Mockito.any(Produto.class)))
			.thenReturn(produto);
				
		ProdutoDTO dtoSaida = produtoService.inserirProduto(dtoEntrada);
		
		assertThat(dtoSaida).isNotNull().isEqualTo(dtoEntrada);
	}
	
	@Test
	public void inserirProdutoDuplicado() {
		ModelMapper mapper = new ModelMapper();

		String mensagem = "Produto " + produto.getCodigo() + " já existente";
		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenThrow(new RecursoAlreadyExistsException(mensagem, produto.getCodigo(), "/v1/produtos"));
		
		ProdutoDTO dtoEntrada = mapper.map(produto, ProdutoDTO.class);

		try {
			produtoService.inserirProduto(dtoEntrada);
			fail();
		} catch (RecursoAlreadyExistsException ex) {
			assertEquals("Produto " + dtoEntrada.getCodigo() + " já existente", ex.getMessage());
		}
	}
	
	@Test
	public void atualizarProduto() {
		ModelMapper mapper = new ModelMapper();

		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.of(produto));
	
		ProdutoDTO dtoEntrada = mapper.map(produto, ProdutoDTO.class);
		when(produtoMapper.toEntity(Mockito.any(ProdutoDTO.class)))
			.thenReturn(produto);
		
		when(produtoMapper.toDto(Mockito.any(Produto.class)))
			.thenReturn(dtoEntrada);
		
		dtoEntrada.setDescricao("Livro It - A coisa");
		produtoService.atualizarProduto("LIVRO23040", dtoEntrada);
		
		assertThat(produto.getDescricao()).isNotNull().isEqualTo(dtoEntrada.getDescricao());
	}
	
	@Test
	public void atualizarProdutoInexistente() {
		ModelMapper mapper = new ModelMapper();

		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.empty());
		
		ProdutoDTO dtoEntrada = mapper.map(produto, ProdutoDTO.class);
		
		try {
			produtoService.atualizarProduto("LIVRO23040", dtoEntrada);
			fail();
		} catch (RecursoNotFoundException ex) {
			assertEquals("Produto " + dtoEntrada.getCodigo() + " inexistente", ex.getMessage());
		}
	}
		
	@Test
	public void removerProduto() throws Exception {
		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.of(produto));
		
		produtoService.removerProduto("LIVRO23040");		
	}
	
	@Test
	public void removerProdutoInexistente() throws Exception {
		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.empty());
	
		try {
			produtoService.removerProduto("LIVRO23040");
			fail();
		} catch (RecursoNotFoundException ex) {
			assertEquals("Produto " + produto.getCodigo() + " inexistente", ex.getMessage());
		}		
	}
	
	@Test
	public void buscarProdutoDadoCodigo() throws Exception {
		ModelMapper mapper = new ModelMapper();

		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.of(produto));

		ProdutoDTO dtoProduto = mapper.map(produto, ProdutoDTO.class);
		when(produtoMapper.toDto(Mockito.any(Produto.class)))
			.thenReturn(dtoProduto);		
		
		ProdutoDTO dtoSaida = produtoService.buscarProdutoDadoCodigo("LIVRO23040");
				
		assertThat(dtoSaida).isNotNull().isEqualTo(dtoProduto);
	}
	
	@Test
	public void buscarProdutoDadoCodigoInexistente() {
		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.empty());
		
		try {
			produtoService.buscarProdutoDadoCodigo("LIVRO23040");
			fail();
		} catch (RecursoNotFoundException ex) {
			assertEquals("Produto " + produto.getCodigo() + " inexistente", ex.getMessage());
		}
	}
	
	@Test
	public void listarProdutosDadoDescricao() throws Exception {				
		Page<Produto> pgProdutos = new PageImpl<>(list(produto));
		when(produtoRepository.findAllByDescricao(Mockito.anyString(), Mockito.any(Pageable.class)))
			.thenReturn(pgProdutos);
		
		ModelMapper mapper = new ModelMapper();
		ProdutoDTO dtoProduto = mapper.map(produto, ProdutoDTO.class);	
		
		when(produtoMapper.toDto(Mockito.any(Produto.class)))
			.thenReturn(dtoProduto);
		
		List<ProdutoDTO> produtos = produtoService.listarProdutosDadoDescricao("Hobbit", PageRequest.of(0, 1)).getContent();
		
		assertThat(produtos).isNotEmpty().hasSize(1).contains(dtoProduto);		
	}
	
	@Test
	public void listarProdutosDadoDescricaoInexistente() throws Exception {	
		Page<Produto> pgProdutos = new PageImpl<>(list());
		
		when(produtoRepository.findAllByDescricao(Mockito.anyString(), Mockito.any(Pageable.class)))
			.thenReturn(pgProdutos);		
		
		List<ProdutoDTO> produtos = produtoService.listarProdutosDadoDescricao("Hobbit", PageRequest.of(0, 1)).getContent();
		
		assertThat(produtos).hasSize(0);
	}
	
	@Test
	public void listarProdutosEmBasePopulada() throws Exception {				
		Page<Produto> pgProdutos = new PageImpl<>(list(produto));
		when(produtoRepository.findAll(Mockito.any(Pageable.class)))
			.thenReturn(pgProdutos);
		
		ModelMapper mapper = new ModelMapper();
		ProdutoDTO dtoProduto = mapper.map(produto, ProdutoDTO.class);
		
		when(produtoMapper.toDto(Mockito.any(Produto.class)))
			.thenReturn(dtoProduto);
		
		List<ProdutoDTO> produtos = produtoService.listarProdutos(PageRequest.of(0, 1)).getContent();
		
		assertThat(produtos).isNotEmpty().hasSize(1).contains(dtoProduto);		
	}
	
	@Test
	public void listarProdutosEmBaseVazia() throws Exception {	
		Page<Produto> pgProdutos = new PageImpl<>(list());
		
		when(produtoRepository.findAll(Mockito.any(Pageable.class)))
			.thenReturn(pgProdutos);		
		
		List<ProdutoDTO> produtos = produtoService.listarProdutos(PageRequest.of(0, 1)).getContent();
		
		assertThat(produtos).hasSize(0);
	}
	
	@Test
	public void listarComentariosDadoCodigoProduto() throws Exception {				
		Page<Produto> pgProdutos = new PageImpl<>(list(produto));
		when(produtoRepository.findAll(Mockito.any(Pageable.class)))
			.thenReturn(pgProdutos);
		
		ModelMapper mapper = new ModelMapper();
		ProdutoDTO dtoProduto = mapper.map(produto, ProdutoDTO.class);
		
		when(produtoMapper.toDto(Mockito.any(Produto.class)))
			.thenReturn(dtoProduto);
		
		List<ProdutoDTO> produtos = produtoService.listarProdutos(PageRequest.of(0, 1)).getContent();
		
		assertThat(produtos).isNotEmpty().hasSize(1).contains(dtoProduto);		
	}
	
	@Test
	public void listarComentariosDadoCodigoProdutoInexistente() throws Exception {	
		Page<Produto> pgProdutos = new PageImpl<>(list());
		
		when(produtoRepository.findAll(Mockito.any(Pageable.class)))
			.thenReturn(pgProdutos);		
		
		List<ProdutoDTO> produtos = produtoService.listarProdutos(PageRequest.of(0, 1)).getContent();
		
		assertThat(produtos).hasSize(0);
	}

}
