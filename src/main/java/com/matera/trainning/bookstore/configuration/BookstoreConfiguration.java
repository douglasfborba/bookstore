package com.matera.trainning.bookstore.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookstoreConfiguration {
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();		
	}
			
}
