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

import com.matera.trainning.bookstore.controller.dto.HistoricoDePrecoDTO;
import com.matera.trainning.bookstore.controller.mapper.HistoricoDePrecoMapper;
import com.matera.trainning.bookstore.model.HistoricoDePreco;
import com.matera.trainning.bookstore.model.Produto;
import com.matera.trainning.bookstore.respository.HistoricoDePrecoRepository;
import com.matera.trainning.bookstore.service.exception.RecursoNotFoundException;

@RunWith(SpringRunner.class)
public class HistoricoDePrecoServiceTest {
	
	@InjectMocks
	private HistoricoDePrecoService histDePrecosService;
	
	@Mock
	private HistoricoDePrecoMapper histDePrecosMapper;

	@Mock
	private HistoricoDePrecoRepository histPrecosRepository;
		
	private Produto produto;
	private HistoricoDePreco itemHistPrecos;

	@Before
	public void setUp() {
		produto = new Produto();
		produto.setCodigo("LIVRO23040");
		produto.setDescricao("Livro The Hobbit");
		produto.setPreco(new BigDecimal(57.63));
		produto.setDataCadastro(LocalDate.now());

		itemHistPrecos = new HistoricoDePreco();
		itemHistPrecos.setProduto(produto);
		itemHistPrecos.setPreco(new BigDecimal(57.63));
		itemHistPrecos.setDataHoraAlteracao(LocalDateTime.now());
	}

	@Test
	public void listarItensHistPrecosDadoProduto() throws Exception {
		itemHistPrecos.setProduto(produto);
		
		Page<HistoricoDePreco> pgItensHistPrecos = new PageImpl<>(list(itemHistPrecos));
		when(histPrecosRepository.findAllByProduto(Mockito.any(Produto.class), Mockito.any(Pageable.class)))
			.thenReturn(pgItensHistPrecos);
		
		ModelMapper mapper = new ModelMapper();		
		HistoricoDePrecoDTO dtoItemHistPrecos = mapper.map(itemHistPrecos, HistoricoDePrecoDTO.class);
		
		when(histDePrecosMapper.toDto(Mockito.any(HistoricoDePreco.class)))
			.thenReturn(dtoItemHistPrecos);
		
		List<HistoricoDePrecoDTO> itensHistPrecos = histDePrecosService.listarItensHistPrecosDadoProduto(produto, PageRequest.of(0, 1)).getContent();
		
		assertThat(itensHistPrecos).isNotEmpty().hasSize(1).contains(dtoItemHistPrecos);		
	}
	
	@Test
	public void listarItensHistPrecosDadoProdutoInexistente() throws Exception {				
		Page<HistoricoDePreco> pgItensHistPrecos = new PageImpl<>(list());
		
		when(histPrecosRepository.findAllByProduto(Mockito.any(Produto.class), Mockito.any(Pageable.class)))
			.thenReturn(pgItensHistPrecos);
						
		List<HistoricoDePrecoDTO> itensHistPrecos = histDePrecosService.listarItensHistPrecosDadoProduto(produto, PageRequest.of(0, 1)).getContent();
		
		assertThat(itensHistPrecos).hasSize(0);	
	}
	
	@Test
	public void buscarPrecoMinimoDadoProduto() throws Exception {
		when(histPrecosRepository.findMinPrecoByProduto(Mockito.any(Produto.class)))
			.thenReturn(Optional.of(itemHistPrecos));
	
		ModelMapper mapper = new ModelMapper();		
		HistoricoDePrecoDTO dtoItemHistPrecos = mapper.map(itemHistPrecos, HistoricoDePrecoDTO.class);
		
		when(histDePrecosMapper.toDto(Mockito.any(HistoricoDePreco.class)))
			.thenReturn(dtoItemHistPrecos);
		
		HistoricoDePrecoDTO dtoSaida = histDePrecosService.buscarPrecoMinimoDadoProduto(produto);
		
		assertThat(dtoSaida).isNotNull().isEqualTo(dtoItemHistPrecos);
	}
	
	@Test
	public void buscarPrecoMinimoDadoProdutoInexistente() throws Exception {
		when(histPrecosRepository.findMinPrecoByProduto(Mockito.any(Produto.class)))
			.thenReturn(Optional.empty());
	
		try {
			histDePrecosService.buscarPrecoMinimoDadoProduto(produto);
			fail();
		} catch (RecursoNotFoundException ex) {
			assertEquals("Preço mínimo inexistente", ex.getMessage());
		}	
	}
	
	@Test
	public void buscarPrecoMaximoDadoProduto() throws Exception {
		when(histPrecosRepository.findMaxPrecoByProduto(Mockito.any(Produto.class)))
			.thenReturn(Optional.of(itemHistPrecos));
	
		ModelMapper mapper = new ModelMapper();		
		HistoricoDePrecoDTO dtoItemHistPrecos = mapper.map(itemHistPrecos, HistoricoDePrecoDTO.class);
		
		when(histDePrecosMapper.toDto(Mockito.any(HistoricoDePreco.class)))
			.thenReturn(dtoItemHistPrecos);
		
		HistoricoDePrecoDTO dtoSaida = histDePrecosService.buscarPrecoMaximoDadoProduto(produto);
		
		assertThat(dtoSaida).isNotNull().isEqualTo(dtoItemHistPrecos);
	}
	
	@Test
	public void buscarPrecoMaximoDadoProdutoInexistente() throws Exception {
		when(histPrecosRepository.findMaxPrecoByProduto(Mockito.any(Produto.class)))
			.thenReturn(Optional.empty());
	
		try {
			histDePrecosService.buscarPrecoMaximoDadoProduto(produto);
			fail();
		} catch (RecursoNotFoundException ex) {
			assertEquals("Preço máximo inexistente", ex.getMessage());
		}	
	}
	
	@Test
	public void buscarPrecoMinimoDadoProdutoNoPeriodo() throws Exception {
		when(histPrecosRepository.findMinByProdutoBetweenDates(Mockito.any(Produto.class), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)))
			.thenReturn(Optional.of(itemHistPrecos));
	
		ModelMapper mapper = new ModelMapper();		
		HistoricoDePrecoDTO dtoItemHistPrecos = mapper.map(itemHistPrecos, HistoricoDePrecoDTO.class);
		
		when(histDePrecosMapper.toDto(Mockito.any(HistoricoDePreco.class)))
			.thenReturn(dtoItemHistPrecos);
		
		HistoricoDePrecoDTO dtoSaida = histDePrecosService.buscarPrecoMinimoDadoProdutoNoPeriodo(produto, LocalDate.now(), LocalDate.now().plusDays(1));
		
		assertThat(dtoSaida).isNotNull().isEqualTo(dtoItemHistPrecos);
	}
	
	@Test
	public void buscarPrecoMinimoDadoProdutoNoPeriodoInexistente() throws Exception {
		when(histPrecosRepository.findMinByProdutoBetweenDates(Mockito.any(Produto.class), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)))
			.thenReturn(Optional.empty());
	
		try {
			histDePrecosService.buscarPrecoMinimoDadoProdutoNoPeriodo(produto, LocalDate.now(), LocalDate.now().plusDays(1));
			fail();
		} catch (RecursoNotFoundException ex) {
			assertEquals("Preço mínimo inexistente no período informado", ex.getMessage());
		}	
	}
	
	@Test
	public void buscarPrecoMaximoDadoProdutoNoPeriodo() throws Exception {
		when(histPrecosRepository.findMaxByProdutoBetweenDates(Mockito.any(Produto.class), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)))
			.thenReturn(Optional.of(itemHistPrecos));
	
		ModelMapper mapper = new ModelMapper();		
		HistoricoDePrecoDTO dtoItemHistPrecos = mapper.map(itemHistPrecos, HistoricoDePrecoDTO.class);
		
		when(histDePrecosMapper.toDto(Mockito.any(HistoricoDePreco.class)))
			.thenReturn(dtoItemHistPrecos);
		
		HistoricoDePrecoDTO dtoSaida = histDePrecosService.buscarPrecoMaximoDadoProdutoNoPeriodo(produto, LocalDate.now(), LocalDate.now().plusDays(1));
		
		assertThat(dtoSaida).isNotNull().isEqualTo(dtoItemHistPrecos);
	}
	
	@Test
	public void buscarPrecoMaximoDadoProdutoNoPeriodoInexistente() throws Exception {
		when(histPrecosRepository.findMaxByProdutoBetweenDates(Mockito.any(Produto.class), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)))
			.thenReturn(Optional.empty());
	
		try {
			histDePrecosService.buscarPrecoMaximoDadoProdutoNoPeriodo(produto, LocalDate.now(), LocalDate.now().plusDays(1));
			fail();
		} catch (RecursoNotFoundException ex) {
			assertEquals("Preço máximo inexistente no período informado", ex.getMessage());
		}	
	}
	
}
