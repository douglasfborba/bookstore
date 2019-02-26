package com.matera.trainning.bookstore.controller.mapper;

import static org.mapstruct.factory.Mappers.getMapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

import com.matera.trainning.bookstore.controller.dto.ProdutoDTO;
import com.matera.trainning.bookstore.controller.mapper.decorator.ProdutoMapperDecorator;
import com.matera.trainning.bookstore.model.Produto;

@Mapper(componentModel = "spring")
@DecoratedWith(ProdutoMapperDecorator.class)
public interface ProdutoMapper {

	public Produto toEntity(ProdutoDTO dtoProduto);

	public ProdutoDTO toDto(Produto produto);

	public static ProdutoMapper getInstance() {
		return getMapper(ProdutoMapper.class);
	}
	
}
