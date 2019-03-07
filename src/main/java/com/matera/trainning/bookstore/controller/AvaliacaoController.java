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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(description = "Avaliação APIs", tags = "Avaliação")
@ApiResponses(value = {
        @ApiResponse(code = 401, message = "Erro de autenticação")
})
@RestController
@RequestMapping
public class AvaliacaoController {
	
	@Autowired
	AvaliacaoService avaliacaoService;

	@ApiOperation(value = "Exibe avaliação dado código", notes = "Retorna uma lista de avaliações", response = AvaliacaoDTO.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Recurso encontrado"),
	        @ApiResponse(code = 404, message = "Recurso inexistente")
	})
	@GetMapping(value = "v1/avaliacoes/{codAvaliacao}", produces = "application/json")
	public ResponseEntity<AvaliacaoDTO> buscaAvaliacaoDadoCodigo(@ApiParam(value = "Código da avaliação") @PathVariable String codAvaliacao) {
		AvaliacaoDTO dtoSaida = avaliacaoService.buscarAvaliacaoDadoCodigo(codAvaliacao);
		return ResponseEntity.ok(dtoSaida);	
	}
	
	@ApiOperation(value = "Lista avaliações dado usuário", notes = "Retorna uma lista de avaliações")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "offset", dataType = "integer", paramType = "query", value = "Deslocamento a ser obtido de acordo com a página e o tamanho das páginas subjacentes"),
		@ApiImplicitParam(name = "pageNumber", dataType = "integer", paramType = "query", value = "Número da página retornada"),
	    @ApiImplicitParam(name = "pageSize", dataType = "integer", paramType = "query", value = "Número máximo de itens por página"),
	    @ApiImplicitParam(name = "paged", dataType = "boolean", paramType = "query", value = "Define se os registros serão paginados"),
	    @ApiImplicitParam(name = "sort.sorted", dataType = "boolean", paramType = "query", value = "Define se os registros serão ordenados"),
	    @ApiImplicitParam(name = "sort.unsorted", dataType = "boolean", paramType = "query", value = "Define se os registros não serão ordenados"),
	    @ApiImplicitParam(name = "unpaged", dataType = "boolean", paramType = "query", value = "Define se os registros não serão paginados"),
	    @ApiImplicitParam(name = "usuario", dataType = "string", paramType = "query", value = "Username do usuário")
	})
	@GetMapping(value = "v1/avaliacoes", params = { "usuario" }, produces = "application/json")
	public Page<AvaliacaoDTO> listaAvaliacoesDadoUsuario(@RequestParam(name = "usuario", required = true) String usuario, Pageable pageable) {
		return avaliacaoService.listarAvaliacoesDadoUsuario(usuario, pageable);
	}
	
}
