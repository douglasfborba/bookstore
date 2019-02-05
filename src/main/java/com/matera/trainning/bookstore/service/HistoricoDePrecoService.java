package com.matera.trainning.bookstore.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matera.trainning.bookstore.domain.HistoricoDePreco;
import com.matera.trainning.bookstore.respository.HistoricoDePrecoRepository;
import com.matera.trainning.bookstore.service.exceptions.RegistroAlreadyExistsException;
import com.matera.trainning.bookstore.service.exceptions.RegistroNotFoundException;

@Service
public class HistoricoDePrecoService {

	@Autowired
	private HistoricoDePrecoRepository repository;

	public HistoricoDePreco insert(HistoricoDePreco historico) throws RegistroAlreadyExistsException {
		String codigo = historico.getProduto().getCodigo();
		LocalDateTime dataHoraAlteracao = historico.getDataHoraAlteracao();

		Optional<HistoricoDePreco> opcional = repository.findByProdutoCodigoAndDataHoraAlteracao(codigo, dataHoraAlteracao);

		if (!opcional.isPresent())
			return repository.save(historico);

		throw new RegistroAlreadyExistsException("Histórico já existente");
	}

	public HistoricoDePreco findByProdutoCodigoAndDataHoraAlteracao(String codigo, LocalDateTime dataHoraAlteracao) throws RegistroNotFoundException {
		Optional<HistoricoDePreco> opcional = repository.findByProdutoCodigoAndDataHoraAlteracao(codigo, dataHoraAlteracao);

		if (!opcional.isPresent())
			throw new RegistroNotFoundException("Histórico inexistente");

		return opcional.get();
	}
	
	public List<HistoricoDePreco> findAllByProdutoCodigo(String codigoProduto) {
		return repository.findAllByProdutoCodigo(codigoProduto);
	}	
	
	public List<HistoricoDePreco> findAllByProdutoCodigoWithDataHoraAlteracaoBetween(String codigoProduto, LocalDateTime inicio, LocalDateTime fim) {
		return repository.findAllByProdutoCodigoWithDataHoraAlteracaoBetween(codigoProduto, inicio, fim);
	}

}
