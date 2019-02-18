package com.matera.trainning.bookstore.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit4.SpringRunner;

import com.matera.trainning.bookstore.controller.dto.AvaliacaoDTO;
import com.matera.trainning.bookstore.controller.dto.ProdutoDTO;
import com.matera.trainning.bookstore.model.Avaliacao;
import com.matera.trainning.bookstore.model.Comentario;
import com.matera.trainning.bookstore.model.Produto;
import com.matera.trainning.bookstore.respository.ProdutoRepository;
import com.matera.trainning.bookstore.service.exception.RecursoAlreadyExistsException;

@RunWith(SpringRunner.class)
public class ProdutoServiceTest {

	@InjectMocks
	private ProdutoService produtoService;
	
	@Mock
	private ModelMapper modelMapper;
	
	@Mock
	private ProdutoRepository produtoRepository;
	
	@Mock
	private AvaliacaoService avaliacaoService;
	
	private Produto produto;
	private Comentario comentario;
	private Avaliacao avaliacao;

	@Before
	public void setUp() {
		produtoService.configuraMapper();
		
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
		fail();
		
		ModelMapper mapper = criaAndConfiguraMapper(); 

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
	public void inserirProdutoDuplicado() {
		ModelMapper mapper = criaAndConfiguraMapper(); 

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
	
	private ModelMapper criaAndConfiguraMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.addConverter(AvaliacaoDTO.getConverter());
		return mapper;
	}

	
}
