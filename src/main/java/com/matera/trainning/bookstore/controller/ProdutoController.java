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
	private ProdutoService service;
	
	@PostMapping("v1/produtos")
	public ResponseEntity<ProdutoDTO> inserir(@Valid @RequestBody ProdutoDTO dtoProduto) {
		ProdutoDTO dto = service.inserir(dtoProduto);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(getUriDadoCodigoRecurso(dto.getCodigo()));
		
		return new ResponseEntity<ProdutoDTO>(dto, headers, CREATED);
	}
	
	@PutMapping("v1/produtos/{codigoProduto}")
	@ResponseStatus(code = NO_CONTENT)
	public void atualizar(@PathVariable String codigoProduto, @Valid @RequestBody ProdutoDTO dtoProduto) {
		service.atualizar(codigoProduto, dtoProduto);
	}
	
	@DeleteMapping("v1/produtos/{codigoProduto}")
	@ResponseStatus(code = NO_CONTENT)
	public void remover(@PathVariable String codigoProduto) {
		service.remover(codigoProduto);
	}
	
	@GetMapping("v1/produtos/{codigoProduto}")
	public ResponseEntity<ProdutoDTO> buscarProdutoPeloCodigo(@PathVariable String codigoProduto) {
		ProdutoDTO dtoProduto = service.buscarDadoCodigoDoProduto(codigoProduto);
		return ResponseEntity.ok(dtoProduto);	
	}
	
	@GetMapping(value = "v1/produtos", params = { "descricao" })
	public Page<ProdutoDTO> buscarProdutosDadoDescricao(@RequestParam("descricao") String descricao, Pageable pageable) {
		return service.buscarProdutosDadoDescricao(descricao, pageable);
	}
	
	@GetMapping("v1/produtos")
	public Page<ProdutoDTO> listarProdutos(Pageable pageable) {
		return service.listarTodosOsProdutos(pageable);
	}
	
	@PostMapping("v1/produtos/{codigoProduto}/comentarios")
	public ResponseEntity<ComentarioDTO> inserirComentario(@PathVariable String codigoProduto, @Valid @RequestBody ComentarioDTO dtoComentario) {
		ComentarioDTO dto = service.inserirNovoComentario(codigoProduto, dtoComentario);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(getUriDadoCodigoRecurso(dto.getCodigo()));
		
		return new ResponseEntity<ComentarioDTO>(dto, headers, CREATED);		
	}
	
	@PutMapping("v1/produtos/{codigoProduto}/comentarios/{codigoComentario}")
	public void atualizarComentario(@PathVariable String codigoProduto, @PathVariable String codigoComentario, @Valid @RequestBody ComentarioDTO dtoComentario) {
		service.atualizarComentario(codigoProduto, codigoComentario, dtoComentario);
	}
	
	@DeleteMapping("v1/produtos/{codigoProduto}/comentarios/{codigoComentario}")
	public void removerComentario(@PathVariable String codigoProduto, @PathVariable String codigoComentario) {
		service.removerComentario(codigoProduto, codigoComentario);
	}
	
	@GetMapping("v1/produtos/{codigoProduto}/comentarios/{codigoComentario}")
	public ComentarioDTO buscarComentarioDadoCodigoProdutoAndCodigoComentario(@PathVariable String codigoProduto, @PathVariable String codigoComentario) {
		return service.buscarComentarioDadoCodigoDoProdutoAndCodigoComentario(codigoProduto, codigoComentario);
	}
	
	@GetMapping("v1/produtos/{codigoProduto}/comentarios")
	public Page<ComentarioDTO> listarComentarios(@PathVariable String codigoProduto, Pageable pageable) {
		return service.listarComentariosDadoCodigoDoProduto(codigoProduto, pageable);
	}
	
	@GetMapping("v1/produtos/{codigoProduto}/precos")
	public Page<HistoricoDePrecoDTO> listarHistoricoDePrecos(@PathVariable String codigoProduto, Pageable pageable) {
		return service.listarPrecosDadoCodigoDoProduto(codigoProduto, pageable);
	}
	
	@GetMapping(value = "v1/produtos/{codigoProduto}/precos", params = { "dataInicial", "dataFinal" })
	public Page<HistoricoDePrecoDTO> listasPrecosEntreDataInicialAndDataFinal(@PathVariable String codigoProduto, @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial, @RequestParam("") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal, Pageable pageable) {
		return service.listasPrecosEntreDataInicialAndDataFinal(codigoProduto, dataInicial, dataFinal, pageable);
	}
	
	@GetMapping("v1/produtos/{codigoProduto}/avaliacoes")
	public Page<AvaliacaoDTO> listarAvaliacoes(@PathVariable String codigoProduto, Pageable pageable) {
		return service.listarAvaliacoesDadoCodigoDoProduto(codigoProduto, pageable);
	}
	
	public static final URI getUriDadoCodigoRecurso(String codigo) {
		return fromCurrentRequestUri().path("/{codigo}").buildAndExpand(codigo).toUri();
	}
	
}
