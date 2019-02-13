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
		ProdutoDTO dtoProduto = produtoService.buscarProdutoDadoCodigo(codProduto);
		return ResponseEntity.ok(dtoProduto);	
	}
	
	@GetMapping(value = "v1/produtos", params = { "descricao" })
	public Page<ProdutoDTO> buscarProdutoDadoDescricao(@RequestParam("descricao") String descProduto, Pageable pageable) {
		return produtoService.buscarProdutoDadoDescricao(descProduto, pageable);
	}
	
	
	
	@GetMapping("v1/produtos/{codProduto}/comentarios")
	public Page<ComentarioDTO> listarComentariosDadoCodigoProduto(@PathVariable String codProduto, Pageable pageable) {
		return produtoService.listarComentariosDadoCodigoProduto(codProduto, pageable);
	}
	
	@PostMapping("v1/produtos/{codProduto}/comentarios")
	public ResponseEntity<ComentarioDTO> inserirComentario(@PathVariable String codProduto, @Valid @RequestBody ComentarioDTO dtoEntrada) {
		ComentarioDTO dtoSaida = produtoService.inserirComentario(codProduto, dtoEntrada);
		HttpHeaders headers = configuraHeaderLocation(dtoSaida.getCodigo());		
		return new ResponseEntity<>(dtoSaida, headers, CREATED);		
	}

	private HttpHeaders configuraHeaderLocation(String codigo) {
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(getUriDadoCodigoRecurso(codigo));
		return headers;
	}
	
	private URI getUriDadoCodigoRecurso(String codigo) {
		return fromCurrentRequestUri().path("/{codigo}").buildAndExpand(codigo).toUri();
	}
			
	
	
	@GetMapping("v1/produtos/{codProduto}/historico-precos")
	public Page<HistoricoDePrecoDTO> listarHistoricoDePrecos(@PathVariable String codProduto, Pageable pageable) {
		return produtoService.listarHistoricoDePrecos(codProduto, pageable);
	}
	
	@GetMapping(value = "v1/produtos/{codProduto}/historico-precos", params = { "dtInicio", "dtFim" })
	public Page<HistoricoDePrecoDTO> buscarHistoricoDePrecosNoPeriodo(@PathVariable String codProduto, @RequestParam("dtInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtInicio, @RequestParam("dtFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtFim, Pageable pageable) {
		return produtoService.buscarHistoricoDePrecosNoPeriodo(codProduto, dtInicio, dtFim, pageable);
	}
	
//	@GetMapping(value = "v1/produtos/{codProduto}/historico-precos", params = { "max=1" })
//	public ResponseEntity<ProdutoDTO> buscarPrecoMaximoDadoCodigoProduto(@PathVariable String codProduto) {
//		return produtoService.buscarPrecoMaximoDadoCodigoProduto(codProduto);
//	}

//	@GetMapping("v1/produtos/{codigoProduto}/avaliacoes")
//	public Page<AvaliacaoDTO> listarAvaliacoesDadoProduto(@PathVariable String codigoProduto, Pageable pageable) {
//		return produtoService.listarAvaliacoesDadoCodigoDoProduto(codigoProduto, pageable);
//	}
//	
//	@PostMapping("v1/produtos/{codigoProduto}/avaliacoes")
//	public ResponseEntity<AvaliacaoDTO> avaliarProduto(@PathVariable String codigoProduto, @Valid @RequestBody AvaliacaoDTO dtoAvaliacao) {
//		AvaliacaoDTO dto = produtoService.avaliarProduto(codigoProduto, dtoAvaliacao);
//		
//		HttpHeaders headers = new HttpHeaders();
//		headers.setLocation(getUriDadoCodigoRecurso(dto.getCodigo()));
//		
//		return new ResponseEntity<AvaliacaoDTO>(dto, headers, CREATED);	
//	}
//	
//	@GetMapping("v1/produtos/{codigoProduto}/comentarios/{codigoComentario}/avaliacoes")
//	public Page<AvaliacaoDTO> listarAvaliacoesDadoProdutoAndComentario(@PathVariable String codigoProduto, @PathVariable String codigoComentario, Pageable pageable) {
//		return produtoService.listarAvaliacoesDadoProdutoAndComentario(codigoProduto, codigoComentario, pageable);
//	}
//	
//	@PostMapping("v1/produtos/{codigoProduto}/comentarios/{codigoComentario}/avaliacoes")
//	public AvaliacaoDTO avaliarProduto(@PathVariable String codigoProduto, @PathVariable String codigoComentario, @Valid @RequestBody AvaliacaoDTO dtoAvaliacao) {
//		return produtoService.avaliarComentario(codigoProduto, codigoComentario, dtoAvaliacao);
//	}
//		

	
}
