package com.matera.trainning.bookstore.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.matera.trainning.bookstore.controller.dto.AvaliacaoDTO;
import com.matera.trainning.bookstore.domain.Comentario;
import com.matera.trainning.bookstore.domain.Produto;
import com.matera.trainning.bookstore.respository.AvaliacaoRepository;

@Service
public class AvaliacaoService {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AvaliacaoRepository repository;
	
	public Page<AvaliacaoDTO> findAllByProduto(Produto produto, Pageable pageable) {
		return repository.findAllByProduto(produto, pageable)
				.map(avaliacao -> modelMapper.map(avaliacao, AvaliacaoDTO.class));
	}
	
	public Page<AvaliacaoDTO> findAllByComentario(Comentario comentario, Pageable pageable) {
		return repository.findAllByComentario(comentario, pageable)
				.map(avaliacao -> modelMapper.map(avaliacao, AvaliacaoDTO.class));
	}
	
}
