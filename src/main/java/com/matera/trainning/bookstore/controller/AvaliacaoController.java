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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "Avaliação APIs", tags = "Avaliação")
@RestController
@RequestMapping
public class AvaliacaoController {
	
	@Autowired
	AvaliacaoService avaliacaoService;

	@ApiOperation(value = "Exibe avaliação dado código", notes = "Retorna uma lista de avaliações")
	@GetMapping("v1/avaliacoes/{codAvaliacao}")
	public ResponseEntity<AvaliacaoDTO> buscaAvaliacaoDadoCodigo(@PathVariable String codAvaliacao) {
		AvaliacaoDTO dtoSaida = avaliacaoService.buscarAvaliacaoDadoCodigo(codAvaliacao);
		return ResponseEntity.ok(dtoSaida);	
	}
	
	@ApiOperation(value = "Lista avaliações dado usuário", notes = "Retorna uma lista de avaliações")
	@GetMapping(value = "v1/avaliacoes", params = { "usuario" })
	public Page<AvaliacaoDTO> listaAvaliacoesDadoUsuario(@RequestParam(name = "usuario", required = true) String usuario, Pageable pageable) {
		return avaliacaoService.listarAvaliacoesDadoUsuario(usuario, pageable);
	}
	
}
