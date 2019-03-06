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
import io.swagger.annotations.ApiOperation;

@Api(description = "Produto APIs", tags = "Produto")
@RestController
@RequestMapping
public class ProdutoController {
	
	@Autowired
	private ProdutoService produtoService;
	
	@ApiOperation(value = "Lista todos os produtos", notes = "Retorna uma lista de produtos")
	@GetMapping("v1/produtos")
	public Page<ProdutoDTO> listarProdutos(Pageable pageable) {
		return produtoService.listarProdutos(pageable);
	}
		
	@ApiOperation(value = "Cria produto", notes = "Retorna o produto criado")
	@PostMapping("v1/produtos")
	public ResponseEntity<ProdutoDTO> inserirProduto(@Valid @RequestBody ProdutoDTO dtoEntrada) {
		ProdutoDTO dtoSaida = produtoService.inserirProduto(dtoEntrada);
		HttpHeaders headers = configuraHeaderLocation(dtoSaida.getCodigo(), "/v1/produtos");		
		return new ResponseEntity<>(dtoSaida, headers, CREATED);
	}	
	
	@ApiOperation(value = "Atualiza produto", notes = "Retorna o produto criado")
	@PutMapping("v1/produtos/{codProduto}")
	@ResponseStatus(code = NO_CONTENT)
	public void atualizarProduto(@PathVariable String codProduto, @Valid @RequestBody ProdutoDTO dtoEntrada) {
		produtoService.atualizarProduto(codProduto, dtoEntrada);
	}
	
	@ApiOperation(value = "Remove produto", notes = "Sem retorno")
	@DeleteMapping("v1/produtos/{codProduto}")
	@ResponseStatus(code = NO_CONTENT)
	public void removerProduto(@PathVariable String codProduto) {
		produtoService.removerProduto(codProduto);
	}
	
	@ApiOperation(value = "Buscs produto dado código", notes = "Retorna um produto")
	@GetMapping("v1/produtos/{codProduto}")
	public ResponseEntity<ProdutoDTO> buscarProdutoDadoCodigo(@PathVariable String codProduto) {
		ProdutoDTO dtoSaida = produtoService.buscarProdutoDadoCodigo(codProduto);
		return ResponseEntity.ok(dtoSaida);	
	}
	
	@ApiOperation(value = "Busca produto dado descrição", notes = "Retorna uma lista de produtos")
	@GetMapping(value = "v1/produtos", params = { "descricao" })
	public Page<ProdutoDTO> buscarProdutoDadoDescricao(@RequestParam(name = "descricao", required = true) String descProduto, Pageable pageable) {
		return produtoService.listarProdutosDadoDescricao(descProduto, pageable);
	}
	
	@ApiOperation(value = "Lista produtos com rating maior que valor passado por parâmetro", notes = "Retorna uma lista de produtos")
	@GetMapping(value = "v1/produtos/rating", params = { "gt" })
	public Page<ProdutoDTO> listarProdutosComRatingMaiorQueParam(@RequestParam(name = "gt", required = true) Double rating, Pageable pageable) {
		return produtoService.listarProdutosComRatingMaiorQueParam(rating, pageable);
	}
	
	@GetMapping("v1/produtos/{codProduto}/comentarios")
	public Page<ComentarioDTO> listarComentariosDadoCodigoProduto(@PathVariable String codProduto, Pageable pageable) {
		return produtoService.listarComentariosDadoCodigoProduto(codProduto, pageable);
	}
	
	@ApiOperation(value = "Comenta produto", notes = "Retorna um produto")
	@PostMapping("v1/produtos/{codProduto}/comentarios")
	public ResponseEntity<ComentarioDTO> comentarProduto(@PathVariable String codProduto, @Valid @RequestBody ComentarioDTO dtoEntrada) {
		ComentarioDTO dtoSaida = produtoService.comentarProduto(codProduto, dtoEntrada);
		HttpHeaders headers = configuraHeaderLocation(dtoSaida.getCodigo(), "/v1/comentarios");		
		return new ResponseEntity<>(dtoSaida, headers, CREATED);		
	}
	
	@ApiOperation(value = "Lista histórico de preços dado código produto", notes = "Retorna uma lista de precos")
	@GetMapping("v1/produtos/{codProduto}/historico-precos")
	public Page<HistoricoDePrecoDTO> listarHistoricoDePrecos(@PathVariable String codProduto, Pageable pageable) {
		return produtoService.listarHistoricoDePrecosDadoCodigoProduto(codProduto, pageable);
	}
	
	@ApiOperation(value = "Lista histórico de preços dado código no período", notes = "Retorna uma lista de precos")
	@GetMapping(value = "v1/produtos/{codProduto}/historico-precos", params = { "dtInicio", "dtFim" })
	public Page<HistoricoDePrecoDTO> listarHistoricoDePrecosNoPeriodoDadoCodigoProduto(@PathVariable String codProduto, 
			@RequestParam(name = "dtInicio", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtInicio, 
			@RequestParam(name = "dtFim", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtFim, 
			Pageable pageable) {
		return produtoService.listarHistoricoDePrecosNoPeriodoDadoProduto(codProduto, dtInicio, dtFim, pageable);
	}
	
	@ApiOperation(value = "Busca preço mínimo do produto", notes = "Retorna um preço")
	@GetMapping(value = "v1/produtos/{codProduto}/historico-precos/min")
	public ResponseEntity<HistoricoDePrecoDTO> buscarPrecoMinimoDadoCodigoProduto(@PathVariable String codProduto) {
		HistoricoDePrecoDTO dtoSaida = produtoService.buscarPrecoMinimoDadoCodigoProduto(codProduto);
		return ResponseEntity.ok(dtoSaida);	
	}
	
	@ApiOperation(value = "Busca preço máximo do produto", notes = "Retorna um preço")
	@GetMapping(value = "v1/produtos/{codProduto}/historico-precos/max")
	public ResponseEntity<HistoricoDePrecoDTO> buscarPrecoMaximoDadoCodigoProduto(@PathVariable String codProduto) {
		HistoricoDePrecoDTO dtoSaida = produtoService.buscarPrecoMaximoDadoCodigoProduto(codProduto);
		return ResponseEntity.ok(dtoSaida);	
	}
	
	@ApiOperation(value = "Busca preço mínimo do produto V1", notes = "Retorna um preço")
	@GetMapping(value = "v1/produtos/{codProduto}/historico-precos/min", params = { "dtInicio", "dtFim" })
	public ResponseEntity<HistoricoDePrecoDTO> buscarPrecoMinimoNoIntervaloDadoCodigoProdutoV1(@PathVariable String codProduto, 
			@RequestParam(name = "dtInicio", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtInicio, 
			@RequestParam(name = "dtFim", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtFim, 
			Pageable pageable) {
		HistoricoDePrecoDTO dtoSaida = produtoService.buscarPrecoMinimoNoIntervaloDadoCodigoProdutoV1(codProduto, dtInicio, dtFim);
		return ResponseEntity.ok(dtoSaida);
	}
	
	@ApiOperation(value = "Busca preço máximo do produto no período V1", notes = "Retorna um preço")
	@GetMapping(value = "v1/produtos/{codProduto}/historico-precos/max", params = { "dtInicio", "dtFim" })
	public ResponseEntity<HistoricoDePrecoDTO> buscarPrecoMaximoNoIntervaloDadoCodigoProdutoV1(@PathVariable String codProduto, 
			@RequestParam(name = "dtInicio", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtInicio, 
			@RequestParam(name = "dtFim", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtFim, 
			Pageable pageable) {
		HistoricoDePrecoDTO dtoSaida = produtoService.buscarPrecoMaximoNoIntervaloDadoCodigoProdutoV1(codProduto, dtInicio, dtFim);
		return ResponseEntity.ok(dtoSaida);	
	}	
	
	@ApiOperation(value = "Busca preço mínimo do produto no período V2", notes = "Retorna um preço")
	@GetMapping(value = "v2/produtos/{codProduto}/historico-precos/min", params = { "dtInicio", "dtFim" })
	public ResponseEntity<HistoricoDePrecoDTO> buscarPrecoMinimoNoIntervaloDadoCodigoProdutoV2(@PathVariable String codProduto, 
			@RequestParam(name = "dtInicio", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtInicio, 
			@RequestParam(name = "dtFim", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtFim, 
			Pageable pageable) {
		HistoricoDePrecoDTO dtoSaida = produtoService.buscarPrecoMinimoNoIntervaloDadoCodigoProdutoV2(codProduto, dtInicio, dtFim);
		return ResponseEntity.ok(dtoSaida);
	}
	
	@ApiOperation(value = "Busca preço máximo do produto no período V2", notes = "Retorna um preço")
	@GetMapping(value = "v2/produtos/{codProduto}/historico-precos/max", params = { "dtInicio", "dtFim" })
	public ResponseEntity<HistoricoDePrecoDTO> buscarPrecoMaximoNoIntervaloDadoCodigoProdutoV2(@PathVariable String codProduto, 
			@RequestParam(name = "dtInicio", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtInicio, 
			@RequestParam(name = "dtFim", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtFim, 
			Pageable pageable) {
		HistoricoDePrecoDTO dtoSaida = produtoService.buscarPrecoMaximoNoIntervaloDadoCodigoProdutoV2(codProduto, dtInicio, dtFim);
		return ResponseEntity.ok(dtoSaida);	
	}	

	@ApiOperation(value = "Lista avaliações dado código produto", notes = "Retorna uma lista de avaliações")
	@GetMapping("v1/produtos/{codProduto}/avaliacoes")
	public Page<AvaliacaoDTO> listarAvaliacoesDadoProduto(@PathVariable String codProduto, Pageable pageable) {
		return produtoService.listarAvaliacoesDadoCodigoDoProduto(codProduto, pageable);
	}
	
	@ApiOperation(value = "Avalia produto", notes = "Retorna um produto")
	@PostMapping("v1/produtos/{codProduto}/avaliacoes")
	public ResponseEntity<AvaliacaoDTO> avaliarProduto(@PathVariable String codProduto, @Valid @RequestBody AvaliacaoDTO dtoEntrada) {
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
