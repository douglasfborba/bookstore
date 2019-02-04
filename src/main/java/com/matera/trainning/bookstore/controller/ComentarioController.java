package com.matera.trainning.bookstore.controller;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequestUri;

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

import com.matera.trainning.bookstore.controller.dto.ComentarioDTO;
import com.matera.trainning.bookstore.controller.facade.ComentarioFacade;
import com.matera.trainning.bookstore.service.exceptions.RegistroAlreadyExistsException;
import com.matera.trainning.bookstore.service.exceptions.RegistroNotFoundException;

@RestController
@RequestMapping("/produtos/{codigoProduto}/comentarios")
public class ComentarioController {

	@Autowired
	private ComentarioFacade facade;
	
	@PostMapping
	public ResponseEntity<ComentarioDTO> insert(@PathVariable String codigoProduto, @Valid @RequestBody ComentarioDTO comentarioDto, HttpServletResponse response) {
		try {
			ComentarioDTO dto = facade.insert(codigoProduto, comentarioDto);
			URI uri = fromCurrentRequestUri()
					.path("/{codigo}")
					.buildAndExpand(dto.getCodigo()).toUri();			
			return ResponseEntity.created(uri).body(dto);
		} catch (RegistroAlreadyExistsException ex) {
			throw new ResponseStatusException(CONFLICT, ex.getMessage(), ex);
		} catch (RegistroNotFoundException ex) {
			throw new ResponseStatusException(NOT_FOUND, ex.getMessage(), ex);
		}
	}
	
	@PutMapping("/{codigoComentario}")
	@ResponseStatus(code = NO_CONTENT)
	public void update(@PathVariable String codigoProduto, @PathVariable String codigoComentario, @Valid @RequestBody ComentarioDTO comentarioDto, HttpServletResponse response) {
		try {
			facade.update(codigoProduto, codigoComentario, comentarioDto);
		} catch (RegistroNotFoundException ex) {
			throw new ResponseStatusException(NOT_FOUND, ex.getMessage(), ex);
		}
	}
	
	@DeleteMapping("/{codigoComentario}")
	@ResponseStatus(code = NO_CONTENT)
	public void delete(@PathVariable String codigoProduto, @PathVariable String codigoComentario) {
		try {
			facade.delete(codigoProduto, codigoComentario);
		} catch (RegistroNotFoundException ex) {
			throw new ResponseStatusException(NOT_FOUND, ex.getMessage(), ex);
		}
	}

	@GetMapping("/{codigoComentario}")
	public ResponseEntity<ComentarioDTO> findByProdutoCodigoAndCodigo(@PathVariable String codigoProduto, @PathVariable String codigoComentario) {
		try {
			ComentarioDTO dto = facade.findByProdutoCodigoAndCodigo(codigoProduto, codigoComentario);
			return ResponseEntity.ok(dto);
		} catch (RegistroNotFoundException ex) {
			throw new ResponseStatusException(NOT_FOUND, ex.getMessage(), ex);
		}
	}

	@GetMapping("/search")
	public List<ComentarioDTO> findByProdutoCodigoAndDescricao(@PathVariable String codigoProduto, @RequestParam("descricao") String descricao) {
		return facade.findByProdutoCodigoAndDescricao(codigoProduto, descricao);
	}
	
	@GetMapping
	public List<ComentarioDTO> findByProdutoCodigo(@PathVariable String codigoProduto) {
		return facade.findByProdutoCodigo(codigoProduto);
	}	
		
}
