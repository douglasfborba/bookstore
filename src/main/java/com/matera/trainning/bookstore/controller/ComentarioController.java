package com.matera.trainning.bookstore.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
		ComentarioDTO dtoSaida = comentarioService.buscarComentarioDadoCodigo(codComentario);
		return ResponseEntity.ok(dtoSaida);	
	}
	
	@GetMapping(value = "v1/comentarios", params = { "usuario" })
	public Page<ComentarioDTO> listarComentariosDadoUsuario(@RequestParam(name = "usuario", required = true) String usuComentario, Pageable pageable) {
		return comentarioService.listarComentariosDadoUsuario(usuComentario, pageable);
	}
	
	@GetMapping("v1/comentarios/{codComentario}/avaliacoes")
	public Page<AvaliacaoDTO> listarAvaliacoesDadoComentario(@PathVariable String codComentario, Pageable pageable) {
		return comentarioService.listarAvaliacoesDadoCodigoComentario(codComentario, pageable);
	}

	@PostMapping("v1/comentarios/{codComentario}/avaliacoes")
	public ResponseEntity<AvaliacaoDTO> avaliarComentario(@PathVariable String codComentario, @Valid @RequestBody AvaliacaoDTO dtoEntrada) {
		AvaliacaoDTO dtoSaida = comentarioService.avaliarComentario(codComentario, dtoEntrada);		
		HttpHeaders headers = configuraHeaderLocation(dtoSaida.getCodigo(), "/v1/avaliacoes");		
		return new ResponseEntity<AvaliacaoDTO>(dtoSaida, headers, CREATED);	
	}
	
	private HttpHeaders configuraHeaderLocation(String codigo, String uriRecurso) {
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(getUriDadoCodigoRecurso(codigo, uriRecurso));
		return headers;
	}
	
	private URI getUriDadoCodigoRecurso(String codigo, String uriRecurso) {
		return fromCurrentContextPath().path(uriRecurso).path("/{codigo}").buildAndExpand(codigo).toUri();
	}
	
}
