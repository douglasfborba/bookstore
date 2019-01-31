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
@RequestMapping("/produtos/{codigoProduto}")
public class ComentarioController {

	@Autowired
	private ComentarioService service;

	@PostMapping("/comentarios")
	public ResponseEntity<Comentario> insert(@PathVariable String codigoProduto, @Valid @RequestBody Comentario comentario, HttpServletResponse response) {
		Comentario comentarioSalvo = null;
		try {
			comentarioSalvo = service.insert(codigoProduto, comentario);
			URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
					.buildAndExpand(comentarioSalvo.getCodigo()).toUri();
			
			return ResponseEntity.created(uri).body(comentarioSalvo);
		} catch (RegistroAlreadyExistsException ex) {
			throw new ResponseStatusException(CONFLICT, ex.getMessage(), ex);
		} catch (RegistroNotFoundException ex) {
			throw new ResponseStatusException(NOT_FOUND, ex.getMessage(), ex);
		}
	}
	
	@PutMapping("/comentarios/{codigoComentario}")
	@ResponseStatus(code = NO_CONTENT)
	public void update(@PathVariable String codigoProduto, @PathVariable String codigoComentario, @Valid @RequestBody Comentario comentario, HttpServletResponse response) {
		try {
			service.update(codigoProduto, codigoComentario, comentario);
		} catch (RegistroNotFoundException ex) {
			throw new ResponseStatusException(NOT_FOUND, ex.getMessage(), ex);
		}
	}
	
	@DeleteMapping("/comentarios/{codigoComentario}")
	@ResponseStatus(code = NO_CONTENT)
	public void delete(@PathVariable String codigoProduto, @PathVariable String codigoComentario) {
		try {
			service.delete(codigoProduto, codigoComentario);
		} catch (RegistroNotFoundException ex) {
			throw new ResponseStatusException(NOT_FOUND, ex.getMessage(), ex);
		}
	}

	@GetMapping("/comentarios/{codigoComentario}")
	public ResponseEntity<Comentario> findByProdutoCodigoAndCodigo(@PathVariable String codigoProduto, @PathVariable String codigoComentario) {
		Comentario comentarioSalvo = null;
		try {
			comentarioSalvo = service.findByProdutoCodigoAndCodigo(codigoProduto, codigoComentario);
			return ResponseEntity.ok(comentarioSalvo);
		} catch (RegistroNotFoundException ex) {
			throw new ResponseStatusException(NOT_FOUND, ex.getMessage(), ex);
		}
	}

	@GetMapping("/comentarios/search")
	public List<Comentario> findByProdutoCodigoAndDescricao(@PathVariable String codigoProduto, @RequestParam("descricao") String descricao) {
		return service.findByProdutoCodigoAndDescricao(codigoProduto, descricao);
	}
		
}
