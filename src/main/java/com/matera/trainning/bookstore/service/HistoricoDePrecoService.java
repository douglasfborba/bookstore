package com.matera.trainning.bookstore.service;

import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matera.trainning.bookstore.controller.dto.HistoricoDePrecoDTO;
import com.matera.trainning.bookstore.model.HistoricoDePreco;
import com.matera.trainning.bookstore.model.Produto;
import com.matera.trainning.bookstore.respository.HistoricoDePrecoRepository;
import com.matera.trainning.bookstore.service.exception.RecursoNotFoundException;

@Service
@Transactional(propagation = SUPPORTS, readOnly = true)
public class HistoricoDePrecoService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private HistoricoDePrecoRepository repository;

	public Page<HistoricoDePrecoDTO> listarItensHistPrecosDadoProduto(Produto produto, Pageable pageable) {
		return repository.findAllByProduto(produto, pageable)
				.map(itemHistorico -> modelMapper.map(itemHistorico, HistoricoDePrecoDTO.class));
	}

	public HistoricoDePrecoDTO buscarPrecoMinimoDadoCodigoProduto(Produto produto) {
		HistoricoDePreco itemHistPreco = repository.findMinPrecoByProduto(produto)
				.orElseThrow(() -> new RecursoNotFoundException("Preço mínimo inexistente"));

		return modelMapper.map(itemHistPreco, HistoricoDePrecoDTO.class);
	}

	public HistoricoDePrecoDTO buscarPrecoMaximoDadoCodigoProduto(Produto produto) {
		HistoricoDePreco itemHistPreco = repository.findMaxPrecoByProduto(produto)
				.orElseThrow(() -> new RecursoNotFoundException("Preço mínimo inexistente"));

		return modelMapper.map(itemHistPreco, HistoricoDePrecoDTO.class);
	}

	public HistoricoDePrecoDTO buscarPrecoMinimoDadoProdutoNoPeriodo(Produto produto, LocalDate dtInicial, LocalDate dtFinal) {
		HistoricoDePreco itemHistPreco = repository.findMinByProdutoBetweenDates(produto, dtInicial, dtFinal)
				.orElseThrow(() -> new RecursoNotFoundException("Preço mínimo inexistente no período informado"));

		return modelMapper.map(itemHistPreco, HistoricoDePrecoDTO.class);
	}

	public HistoricoDePrecoDTO buscarPrecoMaximoDadoProdutoNoPeriodo(Produto produto, LocalDate dtInicial, LocalDate dtFinal) {
		HistoricoDePreco itemHistPreco = repository.findMaxByProdutoBetweenDates(produto, dtInicial, dtFinal)
				.orElseThrow(() -> new RecursoNotFoundException("Preço máximo inexistente no período informado"));

		return modelMapper.map(itemHistPreco, HistoricoDePrecoDTO.class);
	}

}
