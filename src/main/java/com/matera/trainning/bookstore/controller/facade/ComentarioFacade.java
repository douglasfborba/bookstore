package com.matera.trainning.bookstore.controller.facade;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matera.trainning.bookstore.controller.dto.ComentarioDTO;
import com.matera.trainning.bookstore.domain.Comentario;
import com.matera.trainning.bookstore.service.ComentarioService;
import com.matera.trainning.bookstore.service.exceptions.RegistroAlreadyExistsException;
import com.matera.trainning.bookstore.service.exceptions.RegistroNotFoundException;

@Component
public class ComentarioFacade {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ComentarioService service;

	public ComentarioDTO insert(String codigoProduto, ComentarioDTO comentarioDto) throws RegistroAlreadyExistsException, RegistroNotFoundException {
		Comentario comentario = modelMapper.map(comentarioDto, Comentario.class);
		return modelMapper.map(service.insert(codigoProduto, comentario), ComentarioDTO.class);
	}

	public void update(String codigoProduto, String codigoComentario, ComentarioDTO comentarioDto) throws RegistroNotFoundException {
		Comentario comentario = modelMapper.map(comentarioDto, Comentario.class);
		service.update(codigoProduto, codigoComentario, comentario);
	}

	public void delete(String codigoProduto, String codigoComentario) throws RegistroNotFoundException {
		service.delete(codigoProduto, codigoComentario);
	}

	public ComentarioDTO findByCodigo(String codigo) throws RegistroNotFoundException {
		return modelMapper.map(service.findByCodigo(codigo), ComentarioDTO.class);
	}

	public ComentarioDTO findByProdutoCodigoAndCodigo(String codigoProduto, String codigoComentario) throws RegistroNotFoundException {
		return modelMapper.map(service.findByProdutoCodigoAndCodigo(codigoProduto, codigoComentario), ComentarioDTO.class);
	}

	public List<ComentarioDTO> findByProdutoCodigoAndDescricao(String codigoProduto, String descricao) {
		return service.findByProdutoCodigoAndDescricao(codigoProduto, descricao).stream()
				.map(comentario -> modelMapper.map(comentario, ComentarioDTO.class))
				.collect(Collectors.toList());
	}

	public List<ComentarioDTO> findByProdutoCodigo(String codigoProduto) {
		return service.findByProdutoCodigo(codigoProduto).stream()
				.map(comentario -> modelMapper.map(comentario, ComentarioDTO.class))
				.collect(Collectors.toList());
	}

	public List<ComentarioDTO> findAll() {
		return service.findAll().stream()
				.map(comentario -> modelMapper.map(comentario, ComentarioDTO.class))
				.collect(Collectors.toList());
	}

}
