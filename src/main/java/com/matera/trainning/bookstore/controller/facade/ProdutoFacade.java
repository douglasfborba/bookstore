package com.matera.trainning.bookstore.controller.facade;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matera.trainning.bookstore.controller.dto.ProdutoDTO;
import com.matera.trainning.bookstore.domain.Produto;
import com.matera.trainning.bookstore.service.ProdutoService;
import com.matera.trainning.bookstore.service.exceptions.RegistroAlreadyExistsException;
import com.matera.trainning.bookstore.service.exceptions.RegistroNotFoundException;

@Component
public class ProdutoFacade {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ProdutoService service;

	public ProdutoDTO insert(ProdutoDTO produtoDto) throws RegistroAlreadyExistsException {
		Produto produto = modelMapper.map(produtoDto, Produto.class);
		return modelMapper.map(service.insert(produto), ProdutoDTO.class);
	}

	public void update(String codigo, ProdutoDTO produtoDto) throws RegistroNotFoundException {
		Produto produto = modelMapper.map(produtoDto, Produto.class);
		service.update(codigo, produto);
	}

	public void delete(String codigo) throws RegistroNotFoundException {
		service.delete(codigo);
	}

	public ProdutoDTO findByCodigo(String codigo) throws RegistroNotFoundException {
		Produto produto = service.findByCodigo(codigo);
		return modelMapper.map(produto, ProdutoDTO.class);
	}

	public List<ProdutoDTO> findByDescricao(String descricao) {
		return service.findByDescricao(descricao).stream()
				.map(produto -> modelMapper.map(produto, ProdutoDTO.class))
				.collect(Collectors.toList());
	}

	public List<ProdutoDTO> findAll() {
		return service.findAll().stream()
				.map(produto -> modelMapper.map(produto, ProdutoDTO.class))
				.collect(Collectors.toList());
	}

}
