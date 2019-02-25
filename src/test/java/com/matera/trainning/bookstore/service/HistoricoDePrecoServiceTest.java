package com.matera.trainning.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

@RunWith(SpringRunner.class)
public class HistoricoDePrecoServiceTest {

	@InjectMocks
	private HistoricoDePrecoService histDePrecosService;
	
	@Mock
	private HistoricoDePrecoMapper histDePrecosMapper;
	
	@Mock
	private HistoricoDePrecoRepository histDePrecosRepository;
	
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
		HistoricoDePrecoMapper mapper = HistoricoDePrecoMapper.INSTANCE;
		
		itemHistPrecos.setProduto(produto);
		
		Page<HistoricoDePreco> pgItensHistPrecos = new PageImpl<>(list(itemHistPrecos));
		when(histDePrecosRepository.findAllByProduto(Mockito.any(Produto.class), Mockito.any(Pageable.class)))
			.thenReturn(pgItensHistPrecos);
		
		List<HistoricoDePrecoDTO> itensHistPrecos = histDePrecosService.listarItensHistPrecosDadoProduto(produto, PageRequest.of(0, 1)).getContent();
		
		assertThat(itensHistPrecos).isNotEmpty().hasSize(1).contains(mapper.toDto(itemHistPrecos));		
	}
	
	@Test
	public void listarItensHistPrecosDadoProdutoInexistente() throws Exception {				
		Page<HistoricoDePreco> pgItensHistPrecos = new PageImpl<>(list());
		
		when(histDePrecosRepository.findAllByProduto(Mockito.any(Produto.class), Mockito.any(Pageable.class)))
			.thenReturn(pgItensHistPrecos);
						
		List<HistoricoDePrecoDTO> itensHistPrecos = histDePrecosService.listarItensHistPrecosDadoProduto(produto, PageRequest.of(0, 1)).getContent();
		
		assertThat(itensHistPrecos).hasSize(0);	
	}
	
}
