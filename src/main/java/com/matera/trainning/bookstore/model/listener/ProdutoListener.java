package com.matera.trainning.bookstore.model.listener;

import javax.persistence.PostLoad;

import org.springframework.beans.factory.annotation.Autowired;

import com.matera.trainning.bookstore.model.Produto;
import com.matera.trainning.bookstore.service.AvaliacaoService;

public class ProdutoListener {

	@Autowired
	private AvaliacaoService avaliacaoService;
	
	@PostLoad
	public void produtoPostLoad(Produto produto) {
		produto.setRating(avaliacaoService.buscarAvaliacaoMediaDadoProduto(produto));
	}
	
}
