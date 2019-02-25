package com.matera.trainning.bookstore.controller.mapper.decorator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.matera.trainning.bookstore.controller.dto.ProdutoDTO;
import com.matera.trainning.bookstore.controller.mapper.ProdutoMapper;
import com.matera.trainning.bookstore.model.Produto;

public abstract class ProdutoMapperDecorator implements ProdutoMapper {

	@Autowired
	@Qualifier("delegate")
	private ProdutoMapper delegate;

	@Override
	public ProdutoDTO toDto(Produto produto) {
		ProdutoDTO dtoProduto = delegate.toDto(produto);

		if (dtoProduto.getRating() == null)
			dtoProduto.setRating(0.0);

		return dtoProduto;
	}

}
