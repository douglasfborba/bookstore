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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.matera.trainning.bookstore.controller.dto.ComentarioDTO;
import com.matera.trainning.bookstore.service.ComentarioService;

@RestController
@RequestMapping
public class ComentarioController {
	
	@Autowired
	private ComentarioService comentarioService;
	
	@PutMapping("v1/comentarios/{codComentario}")
	@ResponseStatus(code = NO_CONTENT)
	public void atualizarComentario(@PathVariable String codComentario, @Valid @RequestBody ComentarioDTO dtoEntrada) {
		comentarioService.atualizarComentario(codComentario, dtoEntrada);
	}
	
	@DeleteMapping("v1/comentarios/{codComentario}")
	@ResponseStatus(code = NO_CONTENT)
	public void removerComentario(@PathVariable String codComentario) {
		comentarioService.removerComentario(codComentario);
	}

	@GetMapping("v1/comentarios/{codComentario}")
	public ResponseEntity<ComentarioDTO> buscarComentarioDadoCodigo(@PathVariable String codComentario) {
		ComentarioDTO dtoComentario = comentarioService.buscarDadoCodigo(codComentario);
		return ResponseEntity.ok(dtoComentario);	
	}
	
	@GetMapping(value = "v1/comentarios", params = { "usuario" })
	public Page<ComentarioDTO> buscarComentarioDadoUsuario(@RequestParam("usuario") String usuComentario, Pageable pageable) {
		return comentarioService.buscarComentarioDadoUsuario(usuComentario, pageable);
	}
		
}
