package com.matera.trainning.bookstore.service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.matera.trainning.bookstore.controller.dto.ComentarioDTO;
import com.matera.trainning.bookstore.domain.Comentario;
import com.matera.trainning.bookstore.domain.Produto;
import com.matera.trainning.bookstore.exception.RecursoNotFoundException;
import com.matera.trainning.bookstore.respository.ComentarioRepository;

@Service
public class ComentarioService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ComentarioRepository repository;

	@PostConstruct
	public void configuraMapper() {
		modelMapper.addConverter(ComentarioDTO.getConverter());
	}
	
	public void atualizarComentario(String codigoComentario, ComentarioDTO dtoComentario) {
		Comentario comentario = repository.findByCodigo(codigoComentario)
				.orElseThrow(() -> new RecursoNotFoundException(codigoComentario));

		comentario.setUsuario(dtoComentario.getUsuario());
		comentario.setDescricao(dtoComentario.getDescricao());

		repository.save(comentario);
	}

	@Transactional
	public void removerComentario(String codigoComentario) {
		Comentario comentario = repository.findByCodigo(codigoComentario)
				.orElseThrow(() -> new RecursoNotFoundException(codigoComentario));

		repository.delete(comentario);
	}

	public ComentarioDTO buscarDadoCodigo(String codigoComentario) {
		Comentario comentario = repository.findByCodigo(codigoComentario)
				.orElseThrow(() -> new RecursoNotFoundException(codigoComentario));
		
		return modelMapper.map(comentario, ComentarioDTO.class);
	}

	public Page<ComentarioDTO> findAllByProduto(Produto produto, Pageable pageable) {
		return repository.findAllByProduto(produto, pageable)
				.map(comentario -> modelMapper.map(comentario, ComentarioDTO.class));
	}

	public Page<ComentarioDTO> listarTodos(Pageable pageable) {
		return repository.findAll(pageable).map(comentario -> modelMapper.map(comentario, ComentarioDTO.class));
	}

	public Page<ComentarioDTO> buscarComentarioDadoUsuario(String usuComentario, Pageable pageable) {
		return repository.findAllByUsuario(usuComentario, pageable)
				.map(comentario -> modelMapper.map(comentario, ComentarioDTO.class));
	}

}
