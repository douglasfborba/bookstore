package com.matera.trainning.bookstore.controller;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.matera.trainning.bookstore.model.Produto;
import com.matera.trainning.bookstore.service.ProdutoService;
import com.matera.trainning.bookstore.service.exceptions.ProdutoAlreadyExistsException;
import com.matera.trainning.bookstore.service.exceptions.ProdutoNotFoundException;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

	@Autowired
	private ProdutoService service;

	@PostMapping
	public ResponseEntity<Produto> insert(@Valid @RequestBody Produto produto, HttpServletResponse response) {
		Produto produtoSalvo = null;
		try {
			produtoSalvo = service.insert(produto);
			
			URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
					.buildAndExpand(produtoSalvo.getCodigo()).toUri();

			return ResponseEntity.created(uri).body(produtoSalvo);
		} catch (ProdutoAlreadyExistsException ex) {
			throw new ResponseStatusException(CONFLICT, ex.getMessage(), ex);
		}
	}

	@PutMapping("/{codigo}")
	@ResponseStatus(code = NO_CONTENT)
	public void updateByCodigo(@PathVariable String codigo, @Valid @RequestBody Produto produto, HttpServletResponse response) {
		try {
			service.updateByCodigo(codigo, produto);
		} catch (ProdutoNotFoundException ex) {
			throw new ResponseStatusException(NOT_FOUND, ex.getMessage(), ex);
		}
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(code = NO_CONTENT)
	public void deleteByCodigo(@PathVariable String codigo) {
		try {
			service.deleteByCodigo(codigo);
		} catch (ProdutoNotFoundException ex) {
			throw new ResponseStatusException(NOT_FOUND, ex.getMessage(), ex);
		}
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Produto> findByCodigo(@PathVariable String codigo) {
		Produto produtoSalvo = null;
		try {
			produtoSalvo = service.findByCodigo(codigo);
			return ResponseEntity.ok(produtoSalvo);
		} catch (ProdutoNotFoundException ex) {
			throw new ResponseStatusException(NOT_FOUND, ex.getMessage(), ex);
		}
	}

	@GetMapping("/search")
	public List<Produto> findByDescricao(@RequestParam("descricao") String descricao) {
		return service.findByDescricao(descricao);
	}

	@GetMapping
	public List<Produto> findAll() {
		return service.findAll();
	}

}
