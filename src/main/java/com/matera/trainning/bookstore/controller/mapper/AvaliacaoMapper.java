package com.matera.trainning.bookstore.controller.mapper;

import static org.mapstruct.factory.Mappers.getMapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

import com.matera.trainning.bookstore.controller.dto.AvaliacaoDTO;
import com.matera.trainning.bookstore.controller.mapper.decorator.AvaliacaoMapperDecorator;
import com.matera.trainning.bookstore.model.Avaliacao;

@Mapper(componentModel = "spring")
@DecoratedWith(AvaliacaoMapperDecorator.class)
public interface AvaliacaoMapper {

	AvaliacaoMapper INSTANCE = getMapper(AvaliacaoMapper.class);
	  
	public Avaliacao toEntity(AvaliacaoDTO dtoAvaliacao);

	public AvaliacaoDTO toDto(Avaliacao avaliacao);
		
}
