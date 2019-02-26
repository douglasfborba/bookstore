package com.matera.trainning.bookstore.controller.mapper;

import static org.mapstruct.factory.Mappers.getMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.matera.trainning.bookstore.controller.dto.HistoricoDePrecoDTO;
import com.matera.trainning.bookstore.model.HistoricoDePreco;

@Mapper(componentModel = "spring")
public interface HistoricoDePrecoMapper {
	
	public HistoricoDePreco toEntity(HistoricoDePrecoDTO dtoItemHistPreco);

	@Mapping(source = "produto.descricao", target = "descricao")
	public HistoricoDePrecoDTO toDto(HistoricoDePreco itemHistPreco);

	public static HistoricoDePrecoMapper getInstance() {
		return getMapper(HistoricoDePrecoMapper.class);
	}
	
}
