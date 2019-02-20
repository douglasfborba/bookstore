package com.matera.trainning.bookstore.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.matera.trainning.bookstore.controller.dto.ComentarioDTO;
import com.matera.trainning.bookstore.controller.dto.ProdutoDTO;
import com.matera.trainning.bookstore.model.Comentario;
import com.matera.trainning.bookstore.model.Produto;

@Configuration
public class BookstoreConfiguration {
	
	@Bean
	public ModelMapper modelMapper() {
		
		PropertyMap<Produto, ProdutoDTO> prodRatingMapper = new PropertyMap<Produto, ProdutoDTO>() {
			protected void configure() {
				Double rating = source.getRating() != null ? source.getRating() : 0.0;
				map().setRating(rating);
			}
		};

		PropertyMap<Comentario, ComentarioDTO> comRatingMapper = new PropertyMap<Comentario, ComentarioDTO>() {
			protected void configure() {
				Double rating = source.getRating() != null ? source.getRating() : 0.0;
				map().setRating(rating);
			}
		};
		 
		ModelMapper mapper = new ModelMapper();
		
//		mapper.addMappings(prodRatingMapper);
		mapper.addMappings(comRatingMapper);
		
		return mapper;
	}
			
}
