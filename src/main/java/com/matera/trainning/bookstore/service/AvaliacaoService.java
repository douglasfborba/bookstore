package com.matera.trainning.bookstore.service;

import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matera.trainning.bookstore.controller.dto.AvaliacaoDTO;
import com.matera.trainning.bookstore.model.Comentario;
import com.matera.trainning.bookstore.model.Produto;
import com.matera.trainning.bookstore.respository.AvaliacaoRepository;

@Service
@Transactional(propagation = SUPPORTS, readOnly = true)
public class AvaliacaoService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private AvaliacaoRepository repository;

	@PostConstruct
	public void configuraMapper() {
		modelMapper.addConverter(AvaliacaoDTO.getConverter());
	}

	public Page<AvaliacaoDTO> findAllByProduto(Produto produto, Pageable pageable) {
		return repository.findAllByProduto(produto, pageable)
				.map(avaliacao -> modelMapper.map(avaliacao, AvaliacaoDTO.class));
	}

	public Page<AvaliacaoDTO> findAllByComentario(Comentario comentario, Pageable pageable) {
		return repository.findAllByComentario(comentario, pageable)
				.map(avaliacao -> modelMapper.map(avaliacao, AvaliacaoDTO.class));
	}

	public Page<AvaliacaoDTO> findAllByUsuario(String usuario, Pageable pageable) {
		return repository.findAllByUsuario(usuario, pageable)
				.map(avaliacao -> modelMapper.map(avaliacao, AvaliacaoDTO.class));
	}

}
