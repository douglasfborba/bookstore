package com.matera.trainning.bookstore.service;

import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matera.trainning.bookstore.controller.dto.AvaliacaoDTO;
import com.matera.trainning.bookstore.controller.mapper.AvaliacaoMapper;
import com.matera.trainning.bookstore.model.Avaliacao;
import com.matera.trainning.bookstore.model.Comentario;
import com.matera.trainning.bookstore.model.Produto;
import com.matera.trainning.bookstore.respository.AvaliacaoRepository;
import com.matera.trainning.bookstore.service.exception.RecursoNotFoundException;

@Service
@Transactional(propagation = SUPPORTS, readOnly = true)
public class AvaliacaoService {

	@Autowired
	private AvaliacaoMapper mapper;

	@Autowired
	private AvaliacaoRepository repository;

	public AvaliacaoDTO buscarAvaliacaoDadoCodigo(String codAvaliacao) {
		Avaliacao avaliacao = repository.findByCodigo(codAvaliacao)
				.orElseThrow(() -> new RecursoNotFoundException("Avaliação " + codAvaliacao + " inexistente"));
		
		return mapper.toDto(avaliacao);
	}
	
	public Page<AvaliacaoDTO> listarAvaliacoesDadoProduto(Produto produto, Pageable pageable) {
		return repository.findAllByProduto(produto, pageable)
				.map(avaliacao -> mapper.toDto(avaliacao));
	}

	public Page<AvaliacaoDTO> listarAvaliacoesDadoComentario(Comentario comentario, Pageable pageable) {
		return repository.findAllByComentario(comentario, pageable)
				.map(avaliacao -> mapper.toDto(avaliacao));
	}

	public Page<AvaliacaoDTO> listarAvaliacoesDadoUsuario(String usuario, Pageable pageable) {
		return repository.findAllByUsuario(usuario, pageable)
				.map(avaliacao -> mapper.toDto(avaliacao));
	}

}
