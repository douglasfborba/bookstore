package com.matera.trainning.bookstore.controller.facade;

import static com.matera.trainning.bookstore.controller.dto.HistoricoDePrecoDTO.getConverter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matera.trainning.bookstore.controller.dto.HistoricoDePrecoDTO;
import com.matera.trainning.bookstore.domain.HistoricoDePreco;
import com.matera.trainning.bookstore.service.HistoricoDePrecoService;
import com.matera.trainning.bookstore.service.exceptions.RegistroNotFoundException;

@Component
public class HistoricoDePrecoFacade {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private HistoricoDePrecoService service;

	public HistoricoDePrecoFacade() {
		modelMapper.addConverter(getConverter());
	}
	
	public HistoricoDePrecoDTO findByProdutoCodigoAndDataHoraAlteracao(String codigoProduto, LocalDateTime dataHoraAlteracao) throws RegistroNotFoundException {
		HistoricoDePreco historicoDePreco = service.findByProdutoCodigoAndDataHoraAlteracao(codigoProduto, dataHoraAlteracao);
		return modelMapper.map(historicoDePreco, HistoricoDePrecoDTO.class);
	}
	
	public List<HistoricoDePrecoDTO> findAllByProdutoCodigo(String codigoProduto) {
		return service.findAllByProdutoCodigo(codigoProduto).stream()
				.map(historico -> modelMapper.map(historico, HistoricoDePrecoDTO.class))
				.collect(Collectors.toList());
	}
	
	public List<HistoricoDePrecoDTO> findAllByProdutoCodigoWithDataHoraAlteracaoBetween(String codigoProduto, LocalDateTime inicio, LocalDateTime fim) {
		return service.findAllByProdutoCodigoWithDataHoraAlteracaoBetween(codigoProduto, inicio, fim).stream()
				.map(historico -> modelMapper.map(historico, HistoricoDePrecoDTO.class))
				.collect(Collectors.toList());
	}

}
