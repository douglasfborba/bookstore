package com.matera.trainning.bookstore.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.matera.trainning.bookstore.controller.dto.HistoricoDePrecoDTO;
import com.matera.trainning.bookstore.controller.facade.HistoricoDePrecoFacade;

@RestController
@RequestMapping("/produtos/{codigoProduto}/precos")
public class HistoricoDePrecoController {

	@Autowired
	private HistoricoDePrecoFacade facade;
	
	@GetMapping("/search")
	public List<HistoricoDePrecoDTO> findAllByPkProdutoCodigoWithDataHoraAlteracaoBetween(@PathVariable String codigoProduto, @RequestParam("dataHoraInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)  LocalDateTime inicio, @RequestParam("dataHoraFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
		return facade.findAllByPkProdutoCodigoWithDataHoraAlteracaoBetween(codigoProduto, inicio, fim);
	}
	
	@GetMapping
	public List<HistoricoDePrecoDTO> findAllByPkProdutoCodigo(@PathVariable String codigoProduto) {
		return facade.findAllByPkProdutoCodigo(codigoProduto);
	}	
	
}
	