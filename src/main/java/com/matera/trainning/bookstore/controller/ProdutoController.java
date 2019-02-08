package com.matera.trainning.bookstore.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequestUri;

import java.net.URI;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
	private ProdutoService service;
	
	@PostMapping("v1/produtos")
	public ResponseEntity<ProdutoDTO> inserir(@Valid @RequestBody ProdutoDTO dtoProduto, HttpServletResponse response) {
		ProdutoDTO dto = service.inserir(dtoProduto);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(getUriDadoCodigoRecurso(dto.getCodigo()));
		
		return new ResponseEntity<ProdutoDTO>(dto, headers, CREATED);
	}
	
	@PutMapping("v1/produtos/{codigoProduto}")
	@ResponseStatus(code = NO_CONTENT)
	public void atualizar(@PathVariable String codigoProduto, @Valid @RequestBody ProdutoDTO dtoProduto, HttpServletResponse response) {
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
	public Collection<ProdutoDTO> buscarProdutosDadoDescricao(@RequestParam("descricao") String descricao) {
		return service.buscarProdutosDadoDescricao(descricao);
	}
	
	@GetMapping("v1/produtos")
	public Collection<ProdutoDTO> listarProdutos() {
		return service.listarTodosOsProdutos();
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
	public Collection<ComentarioDTO> listarComentarios(@PathVariable String codigoProduto) {
		return service.listarComentariosDadoCodigoDoProduto(codigoProduto);
	}
	
	@GetMapping("v1/produtos/{codigoProduto}/precos")
	public Collection<HistoricoDePrecoDTO> listarHistoricoDePrecos(@PathVariable String codigoProduto) {
		return service.listarPrecosDadoCodigoDoProduto(codigoProduto);
	}
	
	private URI getUriDadoCodigoRecurso(String codigo) {
		return fromCurrentRequestUri().path("/{codigo}").buildAndExpand(codigo).toUri();
	}
	
}
