package com.matera.trainning.bookstore.controller;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.matera.trainning.bookstore.controller.dto.ComentarioDTO;
import com.matera.trainning.bookstore.service.ComentarioService;

@RestController
@RequestMapping
public class ComentarioController {
	
	@Autowired
	private ComentarioService service;
	
	@PutMapping("v1/comentarios/{codigoComentario}")
	@ResponseStatus(code = NO_CONTENT)
	public void atualizar(@PathVariable String codigoComentario, @Valid @RequestBody ComentarioDTO dtoComentario) {
		service.atualizar(codigoComentario, dtoComentario);
	}
	
	@DeleteMapping("v1/comentarios/{codigoComentario}")
	@ResponseStatus(code = NO_CONTENT)
	public void remover(@PathVariable String codigoComentario) {
		service.remover(codigoComentario);
	}

	@GetMapping("v1/comentarios/{codigoComentario}")
	public ResponseEntity<ComentarioDTO> buscarDadoCodigo(@PathVariable String codigoComentario) {
		ComentarioDTO dtoComentario = service.buscarDadoCodigo(codigoComentario);
		return ResponseEntity.ok(dtoComentario);	
	}
	
	@GetMapping("v1/comentarios")
	public Page<ComentarioDTO> listarTodos(Pageable pageable) {
		return service.listarTodos(pageable);
	}	
		
}
