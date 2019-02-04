package com.matera.trainning.bookstore.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matera.trainning.bookstore.domain.Historico;
import com.matera.trainning.bookstore.respository.HistoricoRepository;
import com.matera.trainning.bookstore.service.exceptions.RegistroAlreadyExistsException;
import com.matera.trainning.bookstore.service.exceptions.RegistroNotFoundException;

@Service
public class HistoricoService {

	@Autowired
	private HistoricoRepository repository;

	public Historico insert(Historico historico) throws RegistroAlreadyExistsException {
		String codigo = historico.getPk().getProduto().getCodigo();
		LocalDateTime dataHoraAlteracao = historico.getPk().getDataHoraAlteracao();

		Optional<Historico> opcional = repository.findByPkProdutoCodigoAndPkDataHoraAlteracao(codigo, dataHoraAlteracao);

		if (!opcional.isPresent())
			return repository.save(historico);

		throw new RegistroAlreadyExistsException("Histórico já existente");
	}

	public void update(String codigo, Historico historico) {
		throw new UnsupportedOperationException();
	}

	public void delete(String codigo) {
		throw new UnsupportedOperationException();
	}

	public Historico findByPkProdutoCodigoAndDataHoraAlteracao(String codigo, LocalDateTime dataHoraAlteracao) throws RegistroNotFoundException {
		Optional<Historico> opcional = repository.findByPkProdutoCodigoAndPkDataHoraAlteracao(codigo, dataHoraAlteracao);

		if (!opcional.isPresent())
			throw new RegistroNotFoundException("Histórico inexistente");

		return opcional.get();
	}
	
	public List<Historico> findAll() {
		return repository.findAll();
	}

}
