package com.matera.trainning.bookstore.service;

import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matera.trainning.bookstore.controller.dto.HistoricoDePrecoDTO;
import com.matera.trainning.bookstore.controller.mapper.HistoricoDePrecoMapper;
import com.matera.trainning.bookstore.model.HistoricoDePreco;
import com.matera.trainning.bookstore.model.Produto;
import com.matera.trainning.bookstore.respository.HistoricoDePrecoRepository;
import com.matera.trainning.bookstore.service.exception.RecursoNotFoundException;

@Service
@Transactional(propagation = SUPPORTS, readOnly = true)
public class HistoricoDePrecoService {

	@Autowired
	private HistoricoDePrecoMapper histPrecosMapper;

	@Autowired
	private HistoricoDePrecoRepository histPrecosRepository;

	public Page<HistoricoDePrecoDTO> listarItensHistPrecosDadoProduto(Produto produto, Pageable pageable) {
		return histPrecosRepository.findAllByProduto(produto, pageable).map(itemHistPreco -> histPrecosMapper.toDto(itemHistPreco));
	}

	public HistoricoDePrecoDTO buscarPrecoMinimoDadoProduto(Produto produto) {
		HistoricoDePreco itemHistPreco = histPrecosRepository.findMinPrecoByProduto(produto)
				.orElseThrow(() -> new RecursoNotFoundException("Preço mínimo inexistente"));

		return histPrecosMapper.toDto(itemHistPreco);
	}

	public HistoricoDePrecoDTO buscarPrecoMaximoDadoProduto(Produto produto) {
		HistoricoDePreco itemHistPreco = histPrecosRepository.findMaxPrecoByProduto(produto)
				.orElseThrow(() -> new RecursoNotFoundException("Preço máximo inexistente"));

		return histPrecosMapper.toDto(itemHistPreco);
	}

	public HistoricoDePrecoDTO buscarPrecoMinimoDadoProdutoNoPeriodo(Produto produto, LocalDate dtInicial, LocalDate dtFinal) {
		HistoricoDePreco itemHistPreco = histPrecosRepository.findMinByProdutoBetweenDates(produto, dtInicial, dtFinal)
				.orElseThrow(() -> new RecursoNotFoundException("Preço mínimo inexistente no período informado"));

		return histPrecosMapper.toDto(itemHistPreco);
	}

	public HistoricoDePrecoDTO buscarPrecoMaximoDadoProdutoNoPeriodo(Produto produto, LocalDate dtInicial, LocalDate dtFinal) {
		HistoricoDePreco itemHistPreco = histPrecosRepository.findMaxByProdutoBetweenDates(produto, dtInicial, dtFinal)
				.orElseThrow(() -> new RecursoNotFoundException("Preço máximo inexistente no período informado"));

		return histPrecosMapper.toDto(itemHistPreco);
	}

}
