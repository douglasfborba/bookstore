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

import com.matera.trainning.bookstore.model.Comentario;
import com.matera.trainning.bookstore.service.ComentarioService;
import com.matera.trainning.bookstore.service.exceptions.RegistroAlreadyExistsException;
import com.matera.trainning.bookstore.service.exceptions.RegistroNotFoundException;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

	@Autowired
	private ComentarioService service;

	@PostMapping
	public ResponseEntity<Comentario> insert(@Valid @RequestBody Comentario comentario, HttpServletResponse response) {
		Comentario comentarioSalvo = null;
		try {
			comentarioSalvo = service.insert(comentario);
			URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
					.buildAndExpand(comentarioSalvo.getCodigo()).toUri();

			return ResponseEntity.created(uri).body(comentarioSalvo);
		} catch (RegistroAlreadyExistsException ex) {
			throw new ResponseStatusException(CONFLICT, ex.getMessage(), ex);
		}
	}
	
	@PutMapping("/{codigo}")
	@ResponseStatus(code = NO_CONTENT)
	public void update(@PathVariable String codigo, @Valid @RequestBody Comentario comentario, HttpServletResponse response) {
		try {
			service.update(codigo, comentario);
		} catch (RegistroNotFoundException ex) {
			throw new ResponseStatusException(NOT_FOUND, ex.getMessage(), ex);
		}
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(code = NO_CONTENT)
	public void delete(@PathVariable String codigo) {
		try {
			service.delete(codigo);
		} catch (RegistroNotFoundException ex) {
			throw new ResponseStatusException(NOT_FOUND, ex.getMessage(), ex);
		}
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Comentario> findByCodigo(@PathVariable String codigo) {
		Comentario comentarioSalvo = null;
		try {
			comentarioSalvo = service.findByCodigo(codigo);
			return ResponseEntity.ok(comentarioSalvo);
		} catch (RegistroNotFoundException ex) {
			throw new ResponseStatusException(NOT_FOUND, ex.getMessage(), ex);
		}
	}

	@GetMapping(path = "/search", params = "descricao")
	public List<Comentario> findByDescricao(@RequestParam("descricao") String descricao) {
		return service.findByDescricao(descricao);
	}

	@GetMapping(path = "/search", params = "codigoProduto")
	public List<Comentario> findByProdutoCodigo(@RequestParam("codigoProduto") String codigo) {
		return service.findByProdutoCodigo(codigo);
	}

	@GetMapping
	public List<Comentario> findAll() {
		return service.findAll();
	}

}
