package com.matera.trainning.bookstore.service;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matera.trainning.bookstore.model.Comentario;
import com.matera.trainning.bookstore.respository.ComentarioRepository;
import com.matera.trainning.bookstore.service.exceptions.RegistroAlreadyExistsException;
import com.matera.trainning.bookstore.service.exceptions.RegistroNotFoundException;

@Service
public class ComentarioService {

	@Autowired
	private ComentarioRepository repository;

	public Comentario insert(Comentario comentario) throws RegistroAlreadyExistsException {
		LocalDateTime dataHoraAtual = LocalDateTime.now();

		String codigoBase64 = geraCodigoEmBase64(comentario, dataHoraAtual);
		comentario.setCodigo(codigoBase64);

		if (comentario.getDataHoraCriacao() == null)
			comentario.setDataHoraCriacao(dataHoraAtual);

		return repository.save(comentario);
	}

	public void update(String codigo, Comentario comentario) throws RegistroNotFoundException {
		Optional<Comentario> optional = repository.findByCodigo(codigo);

		if (!optional.isPresent())
			throw new RegistroNotFoundException("Comentário inexistente");

		Comentario comentarioSalvo = optional.get();

		comentarioSalvo.setCodigo(comentario.getCodigo());
		comentarioSalvo.setDescricao(comentario.getDescricao());
		comentarioSalvo.setUsuario(comentario.getUsuario());
		comentarioSalvo.setDataHoraCriacao(comentario.getDataHoraCriacao());

		repository.save(comentarioSalvo);
	}

	public void delete(String codigo) throws RegistroNotFoundException {
		Optional<Comentario> opcional = repository.findByCodigo(codigo);

		if (!opcional.isPresent())
			throw new RegistroNotFoundException("Comentário inexistente");

		repository.deleteByCodigo(codigo);
	}

	public Comentario findByCodigo(String codigo) throws RegistroNotFoundException {
		Optional<Comentario> opcional = repository.findByCodigo(codigo);

		if (!opcional.isPresent())
			throw new RegistroNotFoundException("Comentário inexistente");

		return opcional.get();
	}

	public List<Comentario> findByProdutoCodigo(String codigo) {
		return repository.findByProdutoCodigo(codigo);
	}

	public List<Comentario> findByDescricao(String descricao) {
		return repository.findByDescricao(descricao);
	}

	public List<Comentario> findAll() {
		return repository.findAll();
	}

	private String geraCodigoEmBase64(Comentario comentario, LocalDateTime dataHoraAtual) {
		return new String(parseBase64Binary(comentario.getUsuario() + dataHoraAtual.toString()));
	}

}
