package com.matera.trainning.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
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
import com.matera.trainning.bookstore.controller.dto.HistoricoDePrecoDTO;
import com.matera.trainning.bookstore.controller.dto.ProdutoDTO;
import com.matera.trainning.bookstore.controller.mapper.HistoricoDePrecoMapper;
import com.matera.trainning.bookstore.controller.mapper.ProdutoMapper;
import com.matera.trainning.bookstore.model.Avaliacao;
import com.matera.trainning.bookstore.model.Comentario;
import com.matera.trainning.bookstore.model.HistoricoDePreco;
import com.matera.trainning.bookstore.model.Produto;
import com.matera.trainning.bookstore.respository.ProdutoRepository;
import com.matera.trainning.bookstore.service.exception.RecursoAlreadyExistsException;
import com.matera.trainning.bookstore.service.exception.RecursoNotFoundException;

@RunWith(SpringRunner.class)
public class ProdutoServiceTest {

	@InjectMocks
	private ProdutoService produtoService;
	
	@Mock
	private ComentarioService comentarioService;
	
	@Mock
	private AvaliacaoService avaliacaoService;
	
	@Mock
	private HistoricoDePrecoService histPrecosService;
	
	@Mock
	private ProdutoRepository produtoRepository;
	
	@Mock
	private ProdutoMapper produtoMapper;
	
	@Mock
	private HistoricoDePrecoMapper histPrecosMapper;
	
	private Produto produto;
	private Comentario comentario;
	private Avaliacao avaliacao;
	private HistoricoDePreco itemHistPrecos;

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
		
		itemHistPrecos = new HistoricoDePreco();
		itemHistPrecos.setProduto(produto);
		itemHistPrecos.setPreco(new BigDecimal(57.63));
		itemHistPrecos.setDataHoraAlteracao(LocalDateTime.now());
		
		produto.addHistoricoDePreco(itemHistPrecos);
	}
	
	@Test
	public void inserirProduto() throws Exception {		
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
	public void inserirProdutoSemDataCadastro() throws Exception {		
		ModelMapper mapper = new ModelMapper();

		produto.setDataCadastro(null);
		
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
	public void inserirProdutoDuplicado() throws Exception {
		ModelMapper mapper = new ModelMapper();

		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.of(produto));
		
		ProdutoDTO dtoEntrada = mapper.map(produto, ProdutoDTO.class);

		try {
			produtoService.inserirProduto(dtoEntrada);
			fail();
		} catch (RecursoAlreadyExistsException ex) {
			assertEquals("Produto " + dtoEntrada.getCodigo() + " já existente", ex.getMessage());
		}
	}
	
	@Test
	public void atualizarProduto() throws Exception {
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
	public void atualizarProdutoInexistente() throws Exception {
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
	public void buscarProdutoDadoCodigoInexistente() throws Exception {
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
	public void buscarPrecoMinimoDadoCodigoProduto() throws Exception {
		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.of(produto));

		ModelMapper mapper = new ModelMapper();
		HistoricoDePrecoDTO dtoItemHistPrecos = mapper.map(itemHistPrecos, HistoricoDePrecoDTO.class);
				
		when(histPrecosService.buscarPrecoMinimoDadoProduto(Mockito.any(Produto.class)))
			.thenReturn(dtoItemHistPrecos);
		
		HistoricoDePrecoDTO dtoSaida = produtoService.buscarPrecoMinimoDadoCodigoProduto("LIVRO23040");
		
		assertThat(dtoSaida).isNotNull().isEqualTo(dtoItemHistPrecos);
	}	
	
	@Test
	public void buscarPrecoMinimoDadoCodigoProdutoInexistente() throws Exception {
		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.empty());
		
		try {
			produtoService.buscarPrecoMinimoDadoCodigoProduto(produto.getCodigo());
			fail();
		} catch (RecursoNotFoundException ex) {
			assertEquals("Produto " + produto.getCodigo() + " inexistente", ex.getMessage());
		}	
	}
	
	@Test
	public void buscarPrecoMaximoDadoCodigoProduto() throws Exception {
		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.of(produto));

		ModelMapper mapper = new ModelMapper();
		HistoricoDePrecoDTO dtoItemHistPrecos = mapper.map(itemHistPrecos, HistoricoDePrecoDTO.class);
				
		when(histPrecosService.buscarPrecoMaximoDadoProduto(Mockito.any(Produto.class)))
			.thenReturn(dtoItemHistPrecos);
		
		HistoricoDePrecoDTO dtoSaida = produtoService.buscarPrecoMaximoDadoCodigoProduto("LIVRO23040");
		
		assertThat(dtoSaida).isNotNull().isEqualTo(dtoItemHistPrecos);
	}	
	
	@Test
	public void buscarPrecoMaximoDadoCodigoProdutoInexistente() throws Exception {
		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.empty());
		
		try {
			produtoService.buscarPrecoMaximoDadoCodigoProduto(produto.getCodigo());
			fail();
		} catch (RecursoNotFoundException ex) {
			assertEquals("Produto " + produto.getCodigo() + " inexistente", ex.getMessage());
		}	
	}	
	
	@Test
	public void buscarPrecoMinimoNoIntervaloDadoCodigoProdutoV1() throws Exception {
		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.of(produto));
	
		ModelMapper mapper = new ModelMapper();		
		HistoricoDePrecoDTO dtoItemHistPrecos = mapper.map(itemHistPrecos, HistoricoDePrecoDTO.class);
				
		when(histPrecosMapper.toDto(Mockito.any(HistoricoDePreco.class)))
			.thenReturn(dtoItemHistPrecos);
		
		HistoricoDePrecoDTO dtoSaida = produtoService.buscarPrecoMinimoNoIntervaloDadoCodigoProdutoV1(produto.getCodigo(), LocalDate.now(), LocalDate.now().plusDays(1));
		
		assertThat(dtoSaida).isNotNull().isEqualTo(dtoItemHistPrecos);
	}
	
	@Test
	public void buscarPrecoMinimoNoIntervaloDadoCodigoProdutoV1ProdutoInexistente() throws Exception {
		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.empty());
	
		try {
			produtoService.buscarPrecoMinimoNoIntervaloDadoCodigoProdutoV1(produto.getCodigo(), LocalDate.now(), LocalDate.now().plusDays(1));
			fail();
		} catch (RecursoNotFoundException ex) {
			assertEquals("Produto " + produto.getCodigo() + " inexistente", ex.getMessage());
		}	
	}
	
	@Test
	public void buscarPrecoMinimoNoIntervaloDadoCodigoProdutoV1PrecoMaximoInexistente() throws Exception {
		produto.setPrecos(new HashSet<>());

		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.of(produto));
			
		try {
			produtoService.buscarPrecoMinimoNoIntervaloDadoCodigoProdutoV1(produto.getCodigo(), LocalDate.now(), LocalDate.now().plusDays(1));
			fail();
		} catch (RecursoNotFoundException ex) {
			assertEquals("Preço mínimo inexistente no período informado", ex.getMessage());
		}	
	}	
	
	@Test
	public void buscarPrecoMaximoNoIntervaloDadoCodigoProdutoV1() throws Exception {
		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.of(produto));
	
		ModelMapper mapper = new ModelMapper();		
		HistoricoDePrecoDTO dtoItemHistPrecos = mapper.map(itemHistPrecos, HistoricoDePrecoDTO.class);
				
		when(histPrecosMapper.toDto(Mockito.any(HistoricoDePreco.class)))
			.thenReturn(dtoItemHistPrecos);
		
		HistoricoDePrecoDTO dtoSaida = produtoService.buscarPrecoMaximoNoIntervaloDadoCodigoProdutoV1(produto.getCodigo(), LocalDate.now(), LocalDate.now().plusDays(1));
		
		assertThat(dtoSaida).isNotNull().isEqualTo(dtoItemHistPrecos);
	}
	
	@Test
	public void buscarPrecoMaximoNoIntervaloDadoCodigoProdutoV1ProdutoInexistente() throws Exception {
		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.empty());
	
		try {
			produtoService.buscarPrecoMaximoNoIntervaloDadoCodigoProdutoV1(produto.getCodigo(), LocalDate.now(), LocalDate.now().plusDays(1));
			fail();
		} catch (RecursoNotFoundException ex) {
			assertEquals("Produto " + produto.getCodigo() + " inexistente", ex.getMessage());
		}	
	}
	
	@Test
	public void buscarPrecoMaximoNoIntervaloDadoCodigoProdutoV1PrecoMaximoInexistente() throws Exception {
		produto.setPrecos(new HashSet<>());

		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.of(produto));
			
		try {
			produtoService.buscarPrecoMaximoNoIntervaloDadoCodigoProdutoV1(produto.getCodigo(), LocalDate.now(), LocalDate.now().plusDays(1));
			fail();
		} catch (RecursoNotFoundException ex) {
			assertEquals("Preço máximo inexistente no período informado", ex.getMessage());
		}	
	}
	
	@Test
	public void buscarPrecoMinimoNoIntervaloDadoCodigoProdutoV2() throws Exception {
		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.of(produto));
		
		ModelMapper mapper = new ModelMapper();		
		HistoricoDePrecoDTO dtoItemHistPrecos = mapper.map(itemHistPrecos, HistoricoDePrecoDTO.class);

		when(histPrecosService.buscarPrecoMinimoDadoProdutoNoPeriodo(Mockito.any(Produto.class), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)))
			.thenReturn(dtoItemHistPrecos);
		
		HistoricoDePrecoDTO dtoSaida = produtoService.buscarPrecoMinimoNoIntervaloDadoCodigoProdutoV2(produto.getCodigo(), LocalDate.now(), LocalDate.now());
		
		assertThat(dtoSaida).isNotNull().isEqualTo(dtoItemHistPrecos);
	}
	
	@Test
	public void buscarPrecoMinimoNoIntervaloDadoCodigoProdutoV2Inexistente() throws Exception {
		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.empty());
		
		try {
			produtoService.buscarPrecoMinimoNoIntervaloDadoCodigoProdutoV2(produto.getCodigo(), LocalDate.now(), LocalDate.now().plusDays(1));
			fail();
		} catch (RecursoNotFoundException ex) {
			assertEquals("Produto " + produto.getCodigo() + " inexistente", ex.getMessage());
		}	
	}	
	
	@Test
	public void buscarPrecoMaximoNoIntervaloDadoCodigoProdutoV2() throws Exception {
		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.of(produto));
		
		ModelMapper mapper = new ModelMapper();		
		HistoricoDePrecoDTO dtoItemHistPrecos = mapper.map(itemHistPrecos, HistoricoDePrecoDTO.class);

		when(histPrecosService.buscarPrecoMaximoDadoProdutoNoPeriodo(Mockito.any(Produto.class), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)))
			.thenReturn(dtoItemHistPrecos);
		
		HistoricoDePrecoDTO dtoSaida = produtoService.buscarPrecoMaximoNoIntervaloDadoCodigoProdutoV2(produto.getCodigo(), LocalDate.now(), LocalDate.now());
		
		assertThat(dtoSaida).isNotNull().isEqualTo(dtoItemHistPrecos);
	}
	
	@Test
	public void buscarPrecoMaximoNoIntervaloDadoCodigoProdutoV2Inexistente() throws Exception {
		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.empty());
		
		try {
			produtoService.buscarPrecoMaximoNoIntervaloDadoCodigoProdutoV2(produto.getCodigo(), LocalDate.now(), LocalDate.now().plusDays(1));
			fail();
		} catch (RecursoNotFoundException ex) {
			assertEquals("Produto " + produto.getCodigo() + " inexistente", ex.getMessage());
		}	
	}
	
	@Test
	public void listarAvaliacoesDadoCodigoDoProduto() throws Exception {
		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.of(produto));
		
		ModelMapper mapper = new ModelMapper();
		AvaliacaoDTO dtoAvaliacao = mapper.map(avaliacao, AvaliacaoDTO.class);
		
		Page<AvaliacaoDTO> pgAvaliacoes = new PageImpl<>(list(dtoAvaliacao));
		when(avaliacaoService.listarAvaliacoesDadoProduto(Mockito.any(Produto.class), Mockito.any(Pageable.class)))
			.thenReturn(pgAvaliacoes);
		
		List<AvaliacaoDTO> avaliacoes = produtoService.listarAvaliacoesDadoCodigoDoProduto("LIVRO23040", PageRequest.of(0, 1)).getContent();

		assertThat(avaliacoes).isNotEmpty().hasSize(1).contains(dtoAvaliacao);		
	}	
	
	@Test
	public void listarAvaliacoesDadoCodigoDoProdutoInexistente() throws Exception {
		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.empty());
		
		try {
			produtoService.listarAvaliacoesDadoCodigoDoProduto(produto.getCodigo(), PageRequest.of(0, 1));
			fail();
		} catch (RecursoNotFoundException ex) {
			assertEquals("Produto " + produto.getCodigo() + " inexistente", ex.getMessage());
		}	
	}
	
	@Test
	public void listarHistoricoDePrecosDadoCodigoProduto() throws Exception {
		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.of(produto));
		
		ModelMapper mapper = new ModelMapper();
		HistoricoDePrecoDTO dtoItemHistPrecos = mapper.map(itemHistPrecos, HistoricoDePrecoDTO.class);
		
		Page<HistoricoDePrecoDTO> pgItemHistPrecos = new PageImpl<>(list(dtoItemHistPrecos));
		when(histPrecosService.listarItensHistPrecosDadoProduto(Mockito.any(Produto.class), Mockito.any(Pageable.class)))
			.thenReturn(pgItemHistPrecos);
		
		List<HistoricoDePrecoDTO> avaliacoes = produtoService.listarHistoricoDePrecosDadoCodigoProduto("LIVRO23040", PageRequest.of(0, 1)).getContent();

		assertThat(avaliacoes).isNotEmpty().hasSize(1).contains(dtoItemHistPrecos);		
	}	
	
	@Test
	public void listarHistoricoDePrecosDadoCodigoProdutoInexistente() throws Exception {
		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.empty());
		
		try {
			produtoService.listarHistoricoDePrecosDadoCodigoProduto(produto.getCodigo(), PageRequest.of(0, 1));
			fail();
		} catch (RecursoNotFoundException ex) {
			assertEquals("Produto " + produto.getCodigo() + " inexistente", ex.getMessage());
		}	
	}
	
	
	@Test
	public void listarComentariosDadoCodigoProduto() throws Exception {
		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.of(produto));
		
		ModelMapper mapper = new ModelMapper();
		ComentarioDTO dtoComentario = mapper.map(comentario, ComentarioDTO.class);
		
		Page<ComentarioDTO> pgComentarios = new PageImpl<>(list(dtoComentario));
		when(comentarioService.listarComentariosDadoProduto(Mockito.any(Produto.class), Mockito.any(Pageable.class)))
			.thenReturn(pgComentarios);
		
		List<ComentarioDTO> comentarios = produtoService.listarComentariosDadoCodigoProduto("LIVRO23040", PageRequest.of(0, 1)).getContent();

		assertThat(comentarios).isNotEmpty().hasSize(1).contains(dtoComentario);		
	}	
	
	@Test
	public void listarComentariosDadoCodigoProdutoInexistente() throws Exception {
		when(produtoRepository.findByCodigo(Mockito.anyString()))
			.thenReturn(Optional.empty());
		
		try {
			produtoService.listarComentariosDadoCodigoProduto(produto.getCodigo(), PageRequest.of(0, 1));
			fail();
		} catch (RecursoNotFoundException ex) {
			assertEquals("Produto " + produto.getCodigo() + " inexistente", ex.getMessage());
		}	
	}

}
