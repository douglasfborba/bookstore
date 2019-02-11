package com.matera.trainning.bookstore.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.matera.trainning.bookstore.controller.dto.HistoricoDePrecoDTO;
import com.matera.trainning.bookstore.respository.HistoricoDePrecoRepository;

@Service
public class HistoricoDePrecoService {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private HistoricoDePrecoRepository repository;
	
	public Page<HistoricoDePrecoDTO> findAllByProdutoCodigo(String codigoProduto, Pageable pageable) {
		return repository.findAllByProdutoCodigo(codigoProduto, pageable)
				.map(itemHistorico -> modelMapper.map(itemHistorico, HistoricoDePrecoDTO.class));
	}
	
}
