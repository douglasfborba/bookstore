package com.matera.trainning.bookstore.controller.mapper.decorator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.matera.trainning.bookstore.controller.dto.AvaliacaoDTO;
import com.matera.trainning.bookstore.controller.mapper.AvaliacaoMapper;
import com.matera.trainning.bookstore.model.Avaliacao;

public abstract class AvaliacaoMapperDecorator implements AvaliacaoMapper {

	@Autowired
	@Qualifier("delegate")
	private AvaliacaoMapper delegate;

	@Override
	public AvaliacaoDTO toDto(Avaliacao avaliacao) {
		AvaliacaoDTO dtoAvaliacao = delegate.toDto(avaliacao);
		
		if (avaliacao.getProduto() != null)
			dtoAvaliacao.setDescricao(avaliacao.getProduto().getDescricao());
		
		if (avaliacao.getComentario() != null) 
			dtoAvaliacao.setDescricao(avaliacao.getComentario().getDescricao());
		
		return dtoAvaliacao;
	}
	
}
