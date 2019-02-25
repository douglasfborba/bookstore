package com.matera.trainning.bookstore.controller.mapper.decorator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.matera.trainning.bookstore.controller.dto.ComentarioDTO;
import com.matera.trainning.bookstore.controller.mapper.ComentarioMapper;
import com.matera.trainning.bookstore.model.Comentario;

public abstract class ComentarioMapperDecorator implements ComentarioMapper {

	@Autowired
	@Qualifier("delegate")
	private ComentarioMapper delegate;

	@Override
	public ComentarioDTO toDto(Comentario comentario) {
		ComentarioDTO dtoComentario = delegate.toDto(comentario);
		
		if (dtoComentario.getRating() == null)
			dtoComentario.setRating(0.0);
		
		return dtoComentario;
	}	
	
}
