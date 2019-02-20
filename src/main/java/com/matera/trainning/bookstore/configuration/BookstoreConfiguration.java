package com.matera.trainning.bookstore.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.matera.trainning.bookstore.controller.dto.HistoricoDePrecoDTO;
import com.matera.trainning.bookstore.model.HistoricoDePreco;

@Configuration
public class BookstoreConfiguration {
	
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		modelMapper.addMappings(new PropertyMap<HistoricoDePreco, HistoricoDePrecoDTO>() {
			protected void configure() {
				map().setProdutoDescricaoAtual(source.getProduto().getDescricao());				
			}
		});	
		
		return modelMapper;
	}
			
}
