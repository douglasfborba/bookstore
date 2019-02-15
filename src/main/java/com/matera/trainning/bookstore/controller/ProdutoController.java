package com.matera.trainning.bookstore.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequestUri;

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

@RestController
@RequestMapping
public class ProdutoController {
	
	@Autowired
	private ProdutoService produtoService;
	
	@GetMapping("v1/produtos")
	public Page<ProdutoDTO> listarProdutos(Pageable pageable) {
		return produtoService.listarProdutos(pageable);
	}
		
	@PostMapping("v1/produtos")
	public ResponseEntity<ProdutoDTO> inserirProduto(@Valid @RequestBody ProdutoDTO dtoEntrada) {
		ProdutoDTO dtoSaida = produtoService.inserirProduto(dtoEntrada);
		HttpHeaders headers = configuraHeaderLocation(dtoSaida.getCodigo());		
		return new ResponseEntity<>(dtoSaida, headers, CREATED);
	}	
	
	@PutMapping("v1/produtos/{codProduto}")
	@ResponseStatus(code = NO_CONTENT)
	public void atualizarProduto(@PathVariable String codProduto, @Valid @RequestBody ProdutoDTO dtoEntrada) {
		produtoService.atualizarProduto(codProduto, dtoEntrada);
	}
	
	@DeleteMapping("v1/produtos/{codProduto}")
	@ResponseStatus(code = NO_CONTENT)
	public void removerProduto(@PathVariable String codProduto) {
		produtoService.removerProduto(codProduto);
	}
	
	@GetMapping("v1/produtos/{codProduto}")
	public ResponseEntity<ProdutoDTO> buscarProdutoDadoCodigo(@PathVariable String codProduto) {
		ProdutoDTO dtoSaida = produtoService.buscarProdutoDadoCodigo(codProduto);
		return ResponseEntity.ok(dtoSaida);	
	}
	
	@GetMapping(value = "v1/produtos", params = { "descricao" })
	public Page<ProdutoDTO> buscarProdutoDadoDescricao(@RequestParam(name = "descricao", required = true) String descProduto, Pageable pageable) {
		return produtoService.buscarProdutoDadoDescricao(descProduto, pageable);
	}
	
	@GetMapping("v1/produtos/{codProduto}/comentarios")
	public Page<ComentarioDTO> listarComentariosDadoCodigoProduto(@PathVariable String codProduto, Pageable pageable) {
		return produtoService.listarComentariosDadoCodigoProduto(codProduto, pageable);
	}
	
	@PostMapping("v1/produtos/{codProduto}/comentarios")
	public ResponseEntity<ComentarioDTO> comentarProduto(@PathVariable String codProduto, @Valid @RequestBody ComentarioDTO dtoEntrada) {
		ComentarioDTO dtoSaida = produtoService.comentarProduto(codProduto, dtoEntrada);
		HttpHeaders headers = configuraHeaderLocation(dtoSaida.getCodigo());		
		return new ResponseEntity<>(dtoSaida, headers, CREATED);		
	}
	
	@GetMapping("v1/produtos/{codProduto}/historico-precos")
	public Page<HistoricoDePrecoDTO> listarHistoricoDePrecos(@PathVariable String codProduto, Pageable pageable) {
		return produtoService.listarHistoricoDePrecosDadoProduto(codProduto, pageable);
	}
	
	@GetMapping(value = "v1/produtos/{codProduto}/historico-precos", params = { "dtInicio", "dtFim" })
	public Page<HistoricoDePrecoDTO> listarHistoricoDePrecosNoPeriodoDadoCodigoProduto(@PathVariable String codProduto, 
			@RequestParam(name = "dtInicio", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtInicio, 
			@RequestParam(name = "dtFim", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtFim, 
			Pageable pageable) {
		return produtoService.listarHistoricoDePrecosNoPeriodoDadoProduto(codProduto, dtInicio, dtFim, pageable);
	}
	
	@GetMapping(value = "v1/produtos/{codProduto}/historico-precos", params = { "preco=max" })
	public ResponseEntity<HistoricoDePrecoDTO> buscarPrecoMaximoDadoCodigoProduto(@PathVariable String codProduto) {
		HistoricoDePrecoDTO dtoSaida = produtoService.buscarPrecoMaximoDadoCodigoProduto(codProduto);
		return ResponseEntity.ok(dtoSaida);	
	}
	
	@GetMapping(value = "v1/produtos/{codProduto}/historico-precos", params = { "preco=min" })
	public ResponseEntity<HistoricoDePrecoDTO> buscarPrecoMinimoDadoCodigoProduto(@PathVariable String codProduto) {
		HistoricoDePrecoDTO dtoSaida = produtoService.buscarPrecoMinimoDadoCodigoProduto(codProduto);
		return ResponseEntity.ok(dtoSaida);	
	}
	
	@GetMapping(value = "v1/produtos/{codProduto}/historico-precos", params = { "dtInicio", "dtFim" , "preco=max"})
	public ResponseEntity<HistoricoDePrecoDTO> buscarPrecoMaximoNoIntervaloDadoCodigoProduto(@PathVariable String codProduto, 
			@RequestParam(name = "dtInicio", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtInicio, 
			@RequestParam(name = "dtFim", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtFim, 
			Pageable pageable) {
		HistoricoDePrecoDTO dtoSaida = produtoService.buscarPrecoMaximoNoIntervaloDadoCodigoProduto(codProduto, dtInicio, dtFim);
		return ResponseEntity.ok(dtoSaida);	
	}
	
	@GetMapping(value = "v1/produtos/{codProduto}/historico-precos", params = { "dtInicio", "dtFim" , "preco=min"})
	public ResponseEntity<HistoricoDePrecoDTO> buscarPrecoMinimoNoIntervaloDadoCodigoProduto(@PathVariable String codProduto, 
			@RequestParam(name = "dtInicio", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtInicio, 
			@RequestParam(name = "dtFim", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtFim, 
			Pageable pageable) {
		HistoricoDePrecoDTO dtoSaida = produtoService.buscarPrecoMinimoNoIntervaloDadoCodigoProduto(codProduto, dtInicio, dtFim);
		return ResponseEntity.ok(dtoSaida);
	}

	@GetMapping("v1/produtos/{codProduto}/avaliacoes")
	public Page<AvaliacaoDTO> listarAvaliacoesDadoProduto(@PathVariable String codProduto, Pageable pageable) {
		return produtoService.listarAvaliacoesDadoCodigoDoProduto(codProduto, pageable);
	}
	
	@PostMapping("v1/produtos/{codProduto}/avaliacoes")
	public ResponseEntity<AvaliacaoDTO> avaliarProduto(@PathVariable String codProduto, @Valid @RequestBody AvaliacaoDTO dtoEntrada) {
		AvaliacaoDTO dtoSaida = produtoService.avaliarProduto(codProduto, dtoEntrada);		
		HttpHeaders headers = configuraHeaderLocation(dtoSaida.getCodigo());		
		return new ResponseEntity<AvaliacaoDTO>(dtoSaida, headers, CREATED);	
	}
	
	private HttpHeaders configuraHeaderLocation(String codigo) {
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(getUriDadoCodigoRecurso(codigo));
		return headers;
	}
	
	private URI getUriDadoCodigoRecurso(String codigo) {
		return fromCurrentRequestUri().path("/{codigo}").buildAndExpand(codigo).toUri();
	}		

}
