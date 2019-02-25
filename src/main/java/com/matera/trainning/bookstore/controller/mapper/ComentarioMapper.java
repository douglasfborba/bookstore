package com.matera.trainning.bookstore.controller.mapper;

import static org.mapstruct.factory.Mappers.getMapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

import com.matera.trainning.bookstore.controller.dto.ComentarioDTO;
import com.matera.trainning.bookstore.controller.mapper.decorator.ComentarioMapperDecorator;
import com.matera.trainning.bookstore.model.Comentario;

@Mapper(componentModel = "spring")
@DecoratedWith(ComentarioMapperDecorator.class)
public interface ComentarioMapper {

	ComentarioMapper INSTANCE = getMapper(ComentarioMapper.class);

	public Comentario toEntity(ComentarioDTO dtComentario);

	public ComentarioDTO toDto(Comentario comentario);

}
