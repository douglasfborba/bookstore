package com.matera.trainning.bookstore.controller.facade;

import static com.matera.trainning.bookstore.controller.dto.HistoricoDePrecoDTO.getConverter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matera.trainning.bookstore.controller.dto.HistoricoDePrecoDTO;
import com.matera.trainning.bookstore.respository.HistoricoDePrecoRepository;

@Component
public class HistoricoDePrecoFacade {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private HistoricoDePrecoRepository respository;

	public List<HistoricoDePrecoDTO> findAllByPkProdutoCodigoWithDataHoraAlteracaoBetween(String codigoProduto, LocalDateTime inicio, LocalDateTime fim) {
		return respository.findAllByPkProdutoCodigoWithDataHoraAlteracaoBetween(codigoProduto, inicio, fim).stream()
				.map(historico ->  { 
						modelMapper.addConverter(getConverter());
						return modelMapper.map(historico, HistoricoDePrecoDTO.class);
					}).collect(Collectors.toList());
	}

	public List<HistoricoDePrecoDTO> findAllByPkProdutoCodigo(String codigoProduto) {
		return respository.findAllByPkProdutoCodigo(codigoProduto).stream()
				.map(historico ->  { 
						modelMapper.addConverter(getConverter());
						return modelMapper.map(historico, HistoricoDePrecoDTO.class);
					}).collect(Collectors.toList());
	}

}
