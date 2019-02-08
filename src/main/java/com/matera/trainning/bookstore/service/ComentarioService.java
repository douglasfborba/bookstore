package com.matera.trainning.bookstore.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.matera.trainning.bookstore.controller.dto.ComentarioDTO;
import com.matera.trainning.bookstore.domain.Comentario;
import com.matera.trainning.bookstore.respository.ComentarioRepository;

@Service
public class ComentarioService {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ComentarioRepository repository;
	
	public void atualizar(String codigoComentario, ComentarioDTO dtoComentario) {
		Comentario comentarioSalvo = repository.findByCodigo(codigoComentario)
				.orElseThrow(() -> new ResourceNotFoundException());
		
		comentarioSalvo.setUsuario(dtoComentario.getUsuario());
		comentarioSalvo.setDescricao(dtoComentario.getDescricao());

		repository.save(comentarioSalvo);
	}

	public void remover(String codigoComentario) {
		repository.findByCodigo(codigoComentario)
				.orElseThrow(() -> new ResourceNotFoundException());

		repository.deleteByCodigo(codigoComentario);
	}

	public ComentarioDTO buscarDadoCodigoDoComentario(String codigoComentario) {
		Comentario comentario = repository.findByCodigo(codigoComentario)
				.orElseThrow(() -> new ResourceNotFoundException());

		return modelMapper.map(comentario, ComentarioDTO.class);
	}

	public Collection<ComentarioDTO> listarTodosOsComentarios() {
		return repository.findAll().stream()
				.map(comentario -> modelMapper.map(comentario, ComentarioDTO.class))
				.collect(Collectors.toList());
	}

}
