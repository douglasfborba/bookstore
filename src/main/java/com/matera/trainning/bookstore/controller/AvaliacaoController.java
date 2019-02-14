package com.matera.trainning.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.matera.trainning.bookstore.controller.dto.AvaliacaoDTO;
import com.matera.trainning.bookstore.service.AvaliacaoService;

@RestController
@RequestMapping
public class AvaliacaoController {
	
	@Autowired
	AvaliacaoService avaliacaoService;

	@GetMapping("v1/avaliacoes/{codAvaliacao}")
	public ResponseEntity<AvaliacaoDTO> buscarProdutoDadoCodigo(@PathVariable String codAvaliacao) {
		AvaliacaoDTO dtoSaida = avaliacaoService.buscarAvaliacaoDadoCodigo(codAvaliacao);
		return ResponseEntity.ok(dtoSaida);	
	}
	
	@GetMapping(value = "v1/avaliacoes")
	public Page<AvaliacaoDTO> listaAvaliacoesPorUsuario(@RequestParam(name = "usuario", required = true) String usuario, Pageable pageable) {
		return avaliacaoService.listaAvaliacoesPorUsuario(usuario, pageable);
	}
	
}
