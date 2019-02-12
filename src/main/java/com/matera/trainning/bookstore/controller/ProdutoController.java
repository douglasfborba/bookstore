package com.matera.trainning.bookstore.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequestUri;

import java.net.URI;
import java.time.LocalDate;

import javax.servlet.http.HttpServletResponse;
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
		
	@PostMapping("v1/produtos")
	public ResponseEntity<ProdutoDTO> inserir(@Valid @RequestBody ProdutoDTO dtoProduto, HttpServletResponse response) {
		System.out.println("inserir");
		ProdutoDTO dto = produtoService.inserir(dtoProduto);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(getUriDadoCodigoRecurso(dto.getCodigo()));
		
		return new ResponseEntity<ProdutoDTO>(dto, headers, CREATED);
	}
	
	@PutMapping("v1/produtos/{codigoProduto}")
	@ResponseStatus(code = NO_CONTENT)
	public void atualizar(@PathVariable String codigoProduto, @Valid @RequestBody ProdutoDTO dtoProduto) {
		produtoService.atualizar(codigoProduto, dtoProduto);
	}
	
	@DeleteMapping("v1/produtos/{codigoProduto}")
	@ResponseStatus(code = NO_CONTENT)
	public void remover(@PathVariable String codigoProduto) {
		produtoService.remover(codigoProduto);
	}
	
	@GetMapping("v1/produtos/{codigoProduto}")
	public ResponseEntity<ProdutoDTO> buscarDadoCodigo(@PathVariable String codigoProduto) {
		ProdutoDTO dtoProduto = produtoService.buscarDadoCodigo(codigoProduto);
		return ResponseEntity.ok(dtoProduto);	
	}
	
	@GetMapping(value = "v1/produtos", params = { "descricao" })
	public Page<ProdutoDTO> buscarDadoDescricao(@RequestParam("descricao") String descricao, Pageable pageable) {
		return produtoService.buscarDadoDescricao(descricao, pageable);
	}
	
	@GetMapping("v1/produtos")
	public Page<ProdutoDTO> listarTodos(Pageable pageable) {
		System.out.println("listarTodos");
		return produtoService.listarTodos(pageable);
	}
	
	@PostMapping("v1/produtos/{codigoProduto}/comentarios")
	public ResponseEntity<ComentarioDTO> inserirComentario(@PathVariable String codigoProduto, @Valid @RequestBody ComentarioDTO dtoComentario) {
		ComentarioDTO dto = produtoService.comentarProduto(codigoProduto, dtoComentario);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(getUriDadoCodigoRecurso(dto.getCodigo()));
		
		return new ResponseEntity<ComentarioDTO>(dto, headers, CREATED);		
	}
	
	@PutMapping("v1/produtos/{codigoProduto}/comentarios/{codigoComentario}")
	public void atualizarComentario(@PathVariable String codigoProduto, @PathVariable String codigoComentario, @Valid @RequestBody ComentarioDTO dtoComentario) {
		produtoService.atualizarComentario(codigoProduto, codigoComentario, dtoComentario);
	}
	
	@DeleteMapping("v1/produtos/{codigoProduto}/comentarios/{codigoComentario}")
	public void removerComentario(@PathVariable String codigoProduto, @PathVariable String codigoComentario) {
		produtoService.removerComentario(codigoProduto, codigoComentario);
	}
	
	@GetMapping("v1/produtos/{codigoProduto}/comentarios/{codigoComentario}")
	public ComentarioDTO buscarComentarioDadoCodigoProdutoAndCodigoComentario(@PathVariable String codigoProduto, @PathVariable String codigoComentario) {
		return produtoService.buscarComentarioDadoCodigoDoProdutoAndCodigoComentario(codigoProduto, codigoComentario);
	}
	
	@GetMapping("v1/produtos/{codigoProduto}/comentarios")
	public Page<ComentarioDTO> listarComentarios(@PathVariable String codigoProduto, Pageable pageable) {
		return produtoService.listarComentariosDadoCodigoDoProduto(codigoProduto, pageable);
	}
	
	@GetMapping("v1/produtos/{codigoProduto}/precos")
	public Page<HistoricoDePrecoDTO> listarHistoricoDePrecos(@PathVariable String codigoProduto, Pageable pageable) {
		return produtoService.listarPrecosDadoCodigoDoProduto(codigoProduto, pageable);
	}
	
	@GetMapping(value = "v1/produtos/{codigoProduto}/precos", params = { "dataInicial", "dataFinal" })
	public Page<HistoricoDePrecoDTO> listasPrecosEntreDataInicialAndDataFinal(@PathVariable String codigoProduto, @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial, @RequestParam("") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal, Pageable pageable) {
		return produtoService.listasPrecosEntreDataInicialAndDataFinal(codigoProduto, dataInicial, dataFinal, pageable);
	}
	
	@GetMapping("v1/produtos/{codigoProduto}/avaliacoes")
	public Page<AvaliacaoDTO> listarAvaliacoesDadoProduto(@PathVariable String codigoProduto, Pageable pageable) {
		return produtoService.listarAvaliacoesDadoCodigoDoProduto(codigoProduto, pageable);
	}
	
	@PostMapping("v1/produtos/{codigoProduto}/avaliacoes")
	public ResponseEntity<AvaliacaoDTO> avaliarProduto(@PathVariable String codigoProduto, @Valid @RequestBody AvaliacaoDTO dtoAvaliacao) {
		AvaliacaoDTO dto = produtoService.avaliarProduto(codigoProduto, dtoAvaliacao);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(getUriDadoCodigoRecurso(dto.getCodigo()));
		
		return new ResponseEntity<AvaliacaoDTO>(dto, headers, CREATED);	
	}
	
	@GetMapping("v1/produtos/{codigoProduto}/comentarios/{codigoComentario}/avaliacoes")
	public Page<AvaliacaoDTO> listarAvaliacoesDadoProdutoAndComentario(@PathVariable String codigoProduto, @PathVariable String codigoComentario, Pageable pageable) {
		return produtoService.listarAvaliacoesDadoProdutoAndComentario(codigoProduto, codigoComentario, pageable);
	}
	
	@PostMapping("v1/produtos/{codigoProduto}/comentarios/{codigoComentario}/avaliacoes")
	public AvaliacaoDTO avaliarProduto(@PathVariable String codigoProduto, @PathVariable String codigoComentario, @Valid @RequestBody AvaliacaoDTO dtoAvaliacao) {
		return produtoService.avaliarComentario(codigoProduto, codigoComentario, dtoAvaliacao);
	}
		
	public static final URI getUriDadoCodigoRecurso(String codigo) {
		return fromCurrentRequestUri().path("/{codigo}").buildAndExpand(codigo).toUri();
	}
	
}
