package com.matera.trainning.bookstore.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

import java.net.URI;
import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.matera.trainning.bookstore.controller.dto.AvaliacaoDTO;
import com.matera.trainning.bookstore.controller.dto.ComentarioDTO;
import com.matera.trainning.bookstore.controller.dto.HistoricoDePrecoDTO;
import com.matera.trainning.bookstore.controller.dto.ProdutoDTO;
import com.matera.trainning.bookstore.service.ProdutoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(description = "Produto APIs", tags = "Produto")
@ApiResponses(value = {
        @ApiResponse(code = 401, message = "Erro de autenticação")
})
@RestController
@RequestMapping
public class ProdutoController {
	
	@Autowired
	private ProdutoService produtoService;
	
	@ApiOperation(value = "Lista todos os produtos", notes = "Retorna uma lista de produtos")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "offset", dataType = "integer", paramType = "query", value = "Deslocamento a ser obtido de acordo com a página e o tamanho das páginas subjacentes"),
		@ApiImplicitParam(name = "pageNumber", dataType = "integer", paramType = "query", value = "Número da página retornada"),
	    @ApiImplicitParam(name = "pageSize", dataType = "integer", paramType = "query", value = "Número máximo de itens por página"),
	    @ApiImplicitParam(name = "paged", dataType = "boolean", paramType = "query", value = "Define se os registros serão paginados"),
	    @ApiImplicitParam(name = "sort.sorted", dataType = "boolean", paramType = "query", value = "Define se os registros serão ordenados"),
	    @ApiImplicitParam(name = "sort.unsorted", dataType = "boolean", paramType = "query", value = "Define se os registros não serão ordenados"),
	    @ApiImplicitParam(name = "unpaged", dataType = "boolean", paramType = "query", value = "Define se os registros não serão paginados"),
	})
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Recurso encontrado"),
	})
	@GetMapping(value = "v1/produtos", produces = "application/json")
	public Page<ProdutoDTO> listarProdutos(Pageable pageable) {
		return produtoService.listarProdutos(pageable);
	}
		
	@ApiOperation(value = "Cria produto", notes = "Retorna o produto criado", response = ProdutoDTO.class)
	@ApiResponses(value = {
	       @ApiResponse(code = 201, message = "Recurso criado"),
	       @ApiResponse(code = 409, message = "Recurso duplicado")
	})
	@PostMapping(value = "v1/produtos", produces = "application/json")
	@ResponseStatus(code = CREATED)
	public ResponseEntity<ProdutoDTO> inserirProduto(@ApiParam(value = "JSON com os dados do produto") @Valid @RequestBody ProdutoDTO dtoEntrada) {
		ProdutoDTO dtoSaida = produtoService.inserirProduto(dtoEntrada);
		HttpHeaders headers = configuraHeaderLocation(dtoSaida.getCodigo(), "/v1/produtos");		
		return new ResponseEntity<>(dtoSaida, headers, CREATED);
	}	
	
	@ApiOperation(value = "Atualiza produto", notes = "Retorna o produto criado")
	@ApiResponses(value = {
	        @ApiResponse(code = 204, message = "Recurso atualizado"),
	        @ApiResponse(code = 404, message = "Recurso inexistente")
	})
	@PutMapping(value = "v1/produtos/{codProduto}", produces = "application/json")
	@ResponseStatus(code = NO_CONTENT)
	public void atualizarProduto(@ApiParam(value = "Código do produto") @PathVariable String codProduto, 
			@ApiParam(value = "JSON com os dados do produto") @Valid @RequestBody ProdutoDTO dtoEntrada) {
		produtoService.atualizarProduto(codProduto, dtoEntrada);
	}
	
	@ApiOperation(value = "Remove produto", notes = "Sem retorno")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Recurso removido"),
	        @ApiResponse(code = 404, message = "Recurso inexistente")
	})
	@DeleteMapping(value = "v1/produtos/{codProduto}", produces = "application/json")
	@ResponseStatus(code = NO_CONTENT)
	public void removerProduto(@ApiParam(value = "Código do produto") @PathVariable String codProduto) {
		produtoService.removerProduto(codProduto);
	}
	
	@ApiOperation(value = "Busca produto dado código", notes = "Retorna um produto", response = ProdutoDTO.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Recurso encontrado"),
	        @ApiResponse(code = 404, message = "Recurso inexistente")
	})
	@GetMapping(value = "v1/produtos/{codProduto}", produces = "application/json")
	public ResponseEntity<ProdutoDTO> buscarProdutoDadoCodigo(@ApiParam(value = "Código do produto") @PathVariable String codProduto) {
		ProdutoDTO dtoSaida = produtoService.buscarProdutoDadoCodigo(codProduto);
		return ResponseEntity.ok(dtoSaida);	
	}
	
	@ApiOperation(value = "Busca produto dado descrição", notes = "Retorna uma lista de produtos")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "offset", dataType = "integer", paramType = "query", value = "Deslocamento a ser obtido de acordo com a página e o tamanho das páginas subjacentes"),
		@ApiImplicitParam(name = "pageNumber", dataType = "integer", paramType = "query", value = "Número da página retornada"),
	    @ApiImplicitParam(name = "pageSize", dataType = "integer", paramType = "query", value = "Número máximo de itens por página"),
	    @ApiImplicitParam(name = "paged", dataType = "boolean", paramType = "query", value = "Define se os registros serão paginados"),
	    @ApiImplicitParam(name = "sort.sorted", dataType = "boolean", paramType = "query", value = "Define se os registros serão ordenados"),
	    @ApiImplicitParam(name = "sort.unsorted", dataType = "boolean", paramType = "query", value = "Define se os registros não serão ordenados"),
	    @ApiImplicitParam(name = "unpaged", dataType = "boolean", paramType = "query", value = "Define se os registros não serão paginados"),
	    @ApiImplicitParam(name = "descricao", dataType = "string", paramType = "query", value = "Descrição do produto")
	})
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Recurso encontrado"),
	})
	@GetMapping(value = "v1/produtos", params = { "descricao" }, produces = "application/json")
	public Page<ProdutoDTO> buscarProdutoDadoDescricao(@RequestParam(name = "descricao", required = true) String descProduto, Pageable pageable) {
		return produtoService.listarProdutosDadoDescricao(descProduto, pageable);
	}
	
	@ApiOperation(value = "Lista produtos com rating maior que valor passado por parâmetro", notes = "Retorna uma lista de produtos")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "offset", dataType = "integer", paramType = "query", value = "Deslocamento a ser obtido de acordo com a página e o tamanho das páginas subjacentes"),
		@ApiImplicitParam(name = "pageNumber", dataType = "integer", paramType = "query", value = "Número da página retornada"),
	    @ApiImplicitParam(name = "pageSize", dataType = "integer", paramType = "query", value = "Número máximo de itens por página"),
	    @ApiImplicitParam(name = "paged", dataType = "boolean", paramType = "query", value = "Define se os registros serão paginados"),
	    @ApiImplicitParam(name = "sort.sorted", dataType = "boolean", paramType = "query", value = "Define se os registros serão ordenados"),
	    @ApiImplicitParam(name = "sort.unsorted", dataType = "boolean", paramType = "query", value = "Define se os registros não serão ordenados"),
	    @ApiImplicitParam(name = "unpaged", dataType = "boolean", paramType = "query", value = "Define se os registros não serão paginados"),
	    @ApiImplicitParam(name = "gt", dataType = "integer", paramType = "query", value = "Rating do produto")
	})
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Recurso encontrado"),
	})
	@GetMapping(value = "v1/produtos/rating", params = { "gt" }, produces = "application/json")
	public Page<ProdutoDTO> listarProdutosComRatingMaiorQueParam(@RequestParam(name = "gt", required = true) Double rating, Pageable pageable) {
		return produtoService.listarProdutosComRatingMaiorQueParam(rating, pageable);
	}
	
	@ApiOperation(value = "Lista comentários dados código do produto", notes = "Retorna uma lista de comentários")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "offset", dataType = "integer", paramType = "query", value = "Deslocamento a ser obtido de acordo com a página e o tamanho das páginas subjacentes"),
		@ApiImplicitParam(name = "pageNumber", dataType = "integer", paramType = "query", value = "Número da página retornada"),
	    @ApiImplicitParam(name = "pageSize", dataType = "integer", paramType = "query", value = "Número máximo de itens por página"),
	    @ApiImplicitParam(name = "paged", dataType = "boolean", paramType = "query", value = "Define se os registros serão paginados"),
	    @ApiImplicitParam(name = "sort.sorted", dataType = "boolean", paramType = "query", value = "Define se os registros serão ordenados"),
	    @ApiImplicitParam(name = "sort.unsorted", dataType = "boolean", paramType = "query", value = "Define se os registros não serão ordenados"),
	    @ApiImplicitParam(name = "unpaged", dataType = "boolean", paramType = "query", value = "Define se os registros não serão paginados"),
	})
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Recurso encontrado"),
	})
	@GetMapping(value = "v1/produtos/{codProduto}/comentarios", produces = "application/json")
	public Page<ComentarioDTO> listarComentariosDadoCodigoProduto(@ApiParam(value = "Código do produto") @PathVariable String codProduto, Pageable pageable) {
		return produtoService.listarComentariosDadoCodigoProduto(codProduto, pageable);
	}
	
	@ApiOperation(value = "Comenta produto", notes = "Retorna um produto", response = ProdutoDTO.class)
	@ApiResponses(value = {
		       @ApiResponse(code = 201, message = "Recurso criado"),
		       @ApiResponse(code = 404, message = "Recurso inexistente"),
		       @ApiResponse(code = 409, message = "Recurso duplicado")
	})
	@PostMapping(value = "v1/produtos/{codProduto}/comentarios", produces = "application/json")
	@ResponseStatus(code = CREATED)
	public ResponseEntity<ComentarioDTO> comentarProduto(@ApiParam(value = "Código do produto") @PathVariable String codProduto, 
			@ApiParam(value = "JSON com os dados do produto") @Valid @RequestBody ComentarioDTO dtoEntrada) {
		ComentarioDTO dtoSaida = produtoService.comentarProduto(codProduto, dtoEntrada);
		HttpHeaders headers = configuraHeaderLocation(dtoSaida.getCodigo(), "/v1/comentarios");		
		return new ResponseEntity<>(dtoSaida, headers, CREATED);		
	}
	
	@ApiOperation(value = "Lista histórico de preços dado código produto", notes = "Retorna uma lista de precos")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "offset", dataType = "integer", paramType = "query", value = "Deslocamento a ser obtido de acordo com a página e o tamanho das páginas subjacentes"),
		@ApiImplicitParam(name = "pageNumber", dataType = "integer", paramType = "query", value = "Número da página retornada"),
	    @ApiImplicitParam(name = "pageSize", dataType = "integer", paramType = "query", value = "Número máximo de itens por página"),
	    @ApiImplicitParam(name = "paged", dataType = "boolean", paramType = "query", value = "Define se os registros serão paginados"),
	    @ApiImplicitParam(name = "sort.sorted", dataType = "boolean", paramType = "query", value = "Define se os registros serão ordenados"),
	    @ApiImplicitParam(name = "sort.unsorted", dataType = "boolean", paramType = "query", value = "Define se os registros não serão ordenados"),
	    @ApiImplicitParam(name = "unpaged", dataType = "boolean", paramType = "query", value = "Define se os registros não serão paginados"),
	})
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Recurso encontrado"),
	})
	@GetMapping(value = "v1/produtos/{codProduto}/historico-precos", produces = "application/json")
	public Page<HistoricoDePrecoDTO> listarHistoricoDePrecos(@ApiParam(value = "Código do produto") @PathVariable String codProduto, Pageable pageable) {
		return produtoService.listarHistoricoDePrecosDadoCodigoProduto(codProduto, pageable);
	}
	
	@ApiOperation(value = "Lista histórico de preços dado código no período", notes = "Retorna uma lista de precos")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "offset", dataType = "integer", paramType = "query", value = "Deslocamento a ser obtido de acordo com a página e o tamanho das páginas subjacentes"),
		@ApiImplicitParam(name = "pageNumber", dataType = "integer", paramType = "query", value = "Número da página retornada"),
	    @ApiImplicitParam(name = "pageSize", dataType = "integer", paramType = "query", value = "Número máximo de itens por página"),
	    @ApiImplicitParam(name = "paged", dataType = "boolean", paramType = "query", value = "Define se os registros serão paginados"),
	    @ApiImplicitParam(name = "sort.sorted", dataType = "boolean", paramType = "query", value = "Define se os registros serão ordenados"),
	    @ApiImplicitParam(name = "sort.unsorted", dataType = "boolean", paramType = "query", value = "Define se os registros não serão ordenados"),
	    @ApiImplicitParam(name = "unpaged", dataType = "boolean", paramType = "query", value = "Define se os registros não serão paginados"),
	    @ApiImplicitParam(name = "dtInicio", dataType = "string", paramType = "query", value = "Data inicial do intervalo de pesquisa"),
	    @ApiImplicitParam(name = "dtFim", dataType = "string", paramType = "query", value = "Data final do intervalo de pesquisa")
	})
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Recurso encontrado"),
	})
	@GetMapping(value = "v1/produtos/{codProduto}/historico-precos", params = { "dtInicio", "dtFim" }, produces = "application/json")
	public Page<HistoricoDePrecoDTO> listarHistoricoDePrecosNoPeriodoDadoCodigoProduto(@ApiParam(value = "Código do produto") @PathVariable String codProduto, 
			@RequestParam(name = "dtInicio", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtInicio, 
			@RequestParam(name = "dtFim", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtFim, 
			Pageable pageable) {
		return produtoService.listarHistoricoDePrecosNoPeriodoDadoProduto(codProduto, dtInicio, dtFim, pageable);
	}
	
	@ApiOperation(value = "Busca preço mínimo do produto", notes = "Retorna um preço", response = HistoricoDePrecoDTO.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Recurso encontrado"),
	        @ApiResponse(code = 404, message = "Recurso inexistente")
	})
	@GetMapping(value = "v1/produtos/{codProduto}/historico-precos/min", produces = "application/json")
	public ResponseEntity<HistoricoDePrecoDTO> buscarPrecoMinimoDadoCodigoProduto(@ApiParam(value = "Código do produto") @PathVariable String codProduto) {
		HistoricoDePrecoDTO dtoSaida = produtoService.buscarPrecoMinimoDadoCodigoProduto(codProduto);
		return ResponseEntity.ok(dtoSaida);	
	}
	
	@ApiOperation(value = "Busca preço máximo do produto", notes = "Retorna um preço", response = HistoricoDePrecoDTO.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Recurso encontrado"),
	        @ApiResponse(code = 404, message = "Recurso inexistente")
	})
	@GetMapping(value = "v1/produtos/{codProduto}/historico-precos/max", produces = "application/json")
	public ResponseEntity<HistoricoDePrecoDTO> buscarPrecoMaximoDadoCodigoProduto(@ApiParam(value = "Código do produto") @PathVariable String codProduto) {
		HistoricoDePrecoDTO dtoSaida = produtoService.buscarPrecoMaximoDadoCodigoProduto(codProduto);
		return ResponseEntity.ok(dtoSaida);	
	}
	
	@ApiOperation(value = "Busca preço mínimo do produto V1", notes = "Retorna um preço", response = HistoricoDePrecoDTO.class)
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "dtInicio", dataType = "string", paramType = "query", value = "Data inicial do intervalo de pesquisa"),
	    @ApiImplicitParam(name = "dtFim", dataType = "string", paramType = "query", value = "Data final do intervalo de pesquisa")
	})
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Recurso encontrado"),
	        @ApiResponse(code = 404, message = "Recurso inexistente")
	})
	@GetMapping(value = "v1/produtos/{codProduto}/historico-precos/min", params = { "dtInicio", "dtFim" }, produces = "application/json")
	public ResponseEntity<HistoricoDePrecoDTO> buscarPrecoMinimoNoIntervaloDadoCodigoProdutoV1(@ApiParam(value = "Código do produto") @PathVariable String codProduto, 
			@RequestParam(name = "dtInicio", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtInicio, 
			@RequestParam(name = "dtFim", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtFim) {
		HistoricoDePrecoDTO dtoSaida = produtoService.buscarPrecoMinimoNoIntervaloDadoCodigoProdutoV1(codProduto, dtInicio, dtFim);
		return ResponseEntity.ok(dtoSaida);
	}
	
	@ApiOperation(value = "Busca preço máximo do produto V1", notes = "Retorna um preço", response = HistoricoDePrecoDTO.class)
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "dtInicio", dataType = "string", paramType = "query", value = "Data inicial do intervalo de pesquisa"),
	    @ApiImplicitParam(name = "dtFim", dataType = "string", paramType = "query", value = "Data final do intervalo de pesquisa")
	})
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Recurso encontrado"),
	        @ApiResponse(code = 404, message = "Recurso inexistente")
	})
	@GetMapping(value = "v1/produtos/{codProduto}/historico-precos/max", params = { "dtInicio", "dtFim" }, produces = "application/json")
	public ResponseEntity<HistoricoDePrecoDTO> buscarPrecoMaximoNoIntervaloDadoCodigoProdutoV1(@ApiParam(value = "Código do produto") @PathVariable String codProduto, 
			@RequestParam(name = "dtInicio", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtInicio, 
			@RequestParam(name = "dtFim", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtFim) {
		HistoricoDePrecoDTO dtoSaida = produtoService.buscarPrecoMaximoNoIntervaloDadoCodigoProdutoV1(codProduto, dtInicio, dtFim);
		return ResponseEntity.ok(dtoSaida);	
	}	
	
	@ApiOperation(value = "Busca preço mínimo do produto V2", notes = "Retorna um preço", response = HistoricoDePrecoDTO.class)
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "dtInicio", dataType = "string", paramType = "query", value = "Data inicial do intervalo de pesquisa"),
	    @ApiImplicitParam(name = "dtFim", dataType = "string", paramType = "query", value = "Data final do intervalo de pesquisa")
	})
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Recurso encontrado"),
	        @ApiResponse(code = 404, message = "Recurso inexistente")
	})
	@GetMapping(value = "v2/produtos/{codProduto}/historico-precos/min", params = { "dtInicio", "dtFim" }, produces = "application/json")
	public ResponseEntity<HistoricoDePrecoDTO> buscarPrecoMinimoNoIntervaloDadoCodigoProdutoV2(@ApiParam(value = "Código do produto") @PathVariable String codProduto, 
			@RequestParam(name = "dtInicio", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtInicio, 
			@RequestParam(name = "dtFim", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtFim) {
		HistoricoDePrecoDTO dtoSaida = produtoService.buscarPrecoMinimoNoIntervaloDadoCodigoProdutoV2(codProduto, dtInicio, dtFim);
		return ResponseEntity.ok(dtoSaida);
	}
	
	@ApiOperation(value = "Busca preço máximo do produto no período V2", notes = "Retorna um preço", response = HistoricoDePrecoDTO.class)
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "dtInicio", dataType = "string", paramType = "query", value = "Data inicial do intervalo de pesquisa"),
	    @ApiImplicitParam(name = "dtFim", dataType = "string", paramType = "query", value = "Data final do intervalo de pesquisa")
	})
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Recurso encontrado"),
	        @ApiResponse(code = 404, message = "Recurso inexistente")
	})
	@GetMapping(value = "v2/produtos/{codProduto}/historico-precos/max", params = { "dtInicio", "dtFim" }, produces = "application/json")
	public ResponseEntity<HistoricoDePrecoDTO> buscarPrecoMaximoNoIntervaloDadoCodigoProdutoV2(@ApiParam(value = "Código do produto") @PathVariable String codProduto, 
			@RequestParam(name = "dtInicio", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtInicio, 
			@RequestParam(name = "dtFim", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtFim) {
		HistoricoDePrecoDTO dtoSaida = produtoService.buscarPrecoMaximoNoIntervaloDadoCodigoProdutoV2(codProduto, dtInicio, dtFim);
		return ResponseEntity.ok(dtoSaida);	
	}	

	@ApiOperation(value = "Lista avaliações dado código produto", notes = "Retorna uma lista de avaliações")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "offset", dataType = "integer", paramType = "query", value = "Deslocamento a ser obtido de acordo com a página e o tamanho das páginas subjacentes"),
		@ApiImplicitParam(name = "pageNumber", dataType = "integer", paramType = "query", value = "Número da página retornada"),
	    @ApiImplicitParam(name = "pageSize", dataType = "integer", paramType = "query", value = "Número máximo de itens por página"),
	    @ApiImplicitParam(name = "paged", dataType = "boolean", paramType = "query", value = "Define se os registros serão paginados"),
	    @ApiImplicitParam(name = "sort.sorted", dataType = "boolean", paramType = "query", value = "Define se os registros serão ordenados"),
	    @ApiImplicitParam(name = "sort.unsorted", dataType = "boolean", paramType = "query", value = "Define se os registros não serão ordenados"),
	    @ApiImplicitParam(name = "unpaged", dataType = "boolean", paramType = "query", value = "Define se os registros não serão paginados"),
	})
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Recurso encontrado"),
	        @ApiResponse(code = 404, message = "Recurso inexistente")
	})
	@GetMapping(value = "v1/produtos/{codProduto}/avaliacoes", produces = "application/json")
	public Page<AvaliacaoDTO> listarAvaliacoesDadoProduto(@ApiParam(value = "Código do produto") @PathVariable String codProduto, Pageable pageable) {
		return produtoService.listarAvaliacoesDadoCodigoDoProduto(codProduto, pageable);
	}
	
	@ApiOperation(value = "Avalia produto", notes = "Retorna um produto", response = AvaliacaoDTO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Recurso criado"),
	        @ApiResponse(code = 404, message = "Recurso inexistente"),
		    @ApiResponse(code = 409, message = "Recurso duplicado")
	})
	@PostMapping(value = "v1/produtos/{codProduto}/avaliacoes", produces = "application/json")
	@ResponseStatus(code = CREATED)
	public ResponseEntity<AvaliacaoDTO> avaliarProduto(@ApiParam(value = "Código do produto") @PathVariable String codProduto, 
			@ApiParam(value = "JSON com os dados do produto") @Valid @RequestBody AvaliacaoDTO dtoEntrada) {
		AvaliacaoDTO dtoSaida = produtoService.avaliarProduto(codProduto, dtoEntrada);		
		HttpHeaders headers = configuraHeaderLocation(dtoSaida.getCodigo(), "/v1/avaliacoes");
		return new ResponseEntity<>(dtoSaida, headers, CREATED);	
	}
	
	private HttpHeaders configuraHeaderLocation(String codigo, String uriRecurso) {
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(getUriDadoCodigoRecurso(codigo, uriRecurso));
		return headers;
	}
	
	private URI getUriDadoCodigoRecurso(String codigo, String uriRecurso) {
		return fromCurrentContextPath().path(uriRecurso).path("/{codigo}").buildAndExpand(codigo).toUri();
	}		

}
