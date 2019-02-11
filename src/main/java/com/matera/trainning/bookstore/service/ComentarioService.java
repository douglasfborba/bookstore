package com.matera.trainning.bookstore.service;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.matera.trainning.bookstore.controller.dto.ComentarioDTO;
import com.matera.trainning.bookstore.domain.impl.Comentario;
import com.matera.trainning.bookstore.exception.RecursoNotFoundException;
import com.matera.trainning.bookstore.respository.ComentarioRepository;

@Service
public class ComentarioService {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ComentarioRepository repository;
	
	public void atualizar(String codigoComentario, ComentarioDTO dtoComentario) {
		Comentario comentarioSalvo = repository.findByCodigo(codigoComentario)
				.orElseThrow(() -> new RecursoNotFoundException(codigoComentario));
		
		comentarioSalvo.setUsuario(dtoComentario.getUsuario());
		comentarioSalvo.setDescricao(dtoComentario.getDescricao());

		repository.save(comentarioSalvo);
	}

	@Transactional
	public void remover(String codigoComentario) {
		repository.findByCodigo(codigoComentario)
				.orElseThrow(() -> new RecursoNotFoundException(codigoComentario));

		repository.deleteByCodigo(codigoComentario);
	}

	public ComentarioDTO buscarDadoCodigoDoComentario(String codigoComentario) {
		Comentario comentario = repository.findByCodigo(codigoComentario)
				.orElseThrow(() -> new RecursoNotFoundException(codigoComentario));

		return modelMapper.map(comentario, ComentarioDTO.class);
	}

	public Page<ComentarioDTO> findAllByProdutoCodigo(String codigoProduto, Pageable pageable) {
		return repository.findAllByProdutoCodigo(codigoProduto, pageable)
				.map(comentario -> modelMapper.map(comentario, ComentarioDTO.class));
	}
	
	public Page<ComentarioDTO> listarTodosOsComentarios(Pageable pageable) {
		return repository.findAll(pageable)
				.map(comentario -> modelMapper.map(comentario, ComentarioDTO.class));
	}	

}
