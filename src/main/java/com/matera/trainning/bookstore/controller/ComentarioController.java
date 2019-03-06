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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(description = "Comentário APIs", tags = "Comentário")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "Recurso encontrado com sucesso"),
        @ApiResponse(code = 401, message = "Erro ao autenticar usuário"),
        @ApiResponse(code = 403, message = "Permissão de acesso negada"),
        @ApiResponse(code = 404, message = "Recurso não encontrado")
})
@RestController
@RequestMapping
public class ComentarioController {
	
	@Autowired
	private ComentarioService comentarioService;
	
	@ApiOperation(value = "Atualiza comentário", notes = "Sem retorno")
	@PutMapping("v1/comentarios/{codComentario}")
	@ResponseStatus(code = NO_CONTENT)
	public void atualizarComentario(@PathVariable String codComentario, @Valid @RequestBody ComentarioDTO dtoEntrada) {
		comentarioService.atualizarComentario(codComentario, dtoEntrada);
	}
	
	@ApiOperation(value = "Remove comentário", notes = "Sem retorno")
	@DeleteMapping("v1/comentarios/{codComentario}")
	@ResponseStatus(code = NO_CONTENT)
	public void removerComentario(@PathVariable String codComentario) {
		comentarioService.removerComentario(codComentario);
	}

	@ApiOperation(value = "Busca comentário dado código", notes = "Retorna o comentário buscado")
	@GetMapping("v1/comentarios/{codComentario}")
	public ResponseEntity<ComentarioDTO> buscarComentarioDadoCodigo(@PathVariable String codComentario) {
		ComentarioDTO dtoSaida = comentarioService.buscarComentarioDadoCodigo(codComentario);
		return ResponseEntity.ok(dtoSaida);	
	}
	
	@ApiOperation(value = "Lista comentários dado usuário", notes = "Retorna uma lista de comentários")
	@GetMapping(value = "v1/comentarios", params = { "usuario" })
	public Page<ComentarioDTO> listarComentariosDadoUsuario(@RequestParam(name = "usuario", required = true) String usuComentario, Pageable pageable) {
		return comentarioService.listarComentariosDadoUsuario(usuComentario, pageable);
	}
	
	@ApiOperation(value = "Lista comentários com rating maior que valor passado como parâmetro", notes = "Retorna uma lista de comentários")
	@GetMapping(value = "v1/comentarios/rating", params = { "gt" })
	public Page<ComentarioDTO> listarComentariosComRatingMaiorQueParam(@RequestParam(name = "gt", required = true) Double rating, Pageable pageable) {
		return comentarioService.listarComentariosComRatingMaiorQueParam(rating, pageable);
	}
		
	@ApiOperation(value = "Lista avaliações dado código do comentário", notes = "Retorna uma lista de avaliações")
	@GetMapping("v1/comentarios/{codComentario}/avaliacoes")
	public Page<AvaliacaoDTO> listarAvaliacoesDadoComentario(@PathVariable String codComentario, Pageable pageable) {
		return comentarioService.listarAvaliacoesDadoCodigoComentario(codComentario, pageable);
	}

	@ApiOperation(value = "Avalia comentário", notes = "Retorna a avaliação criada")
	@PostMapping("v1/comentarios/{codComentario}/avaliacoes")
	public ResponseEntity<AvaliacaoDTO> avaliarComentario(@PathVariable String codComentario, @Valid @RequestBody AvaliacaoDTO dtoEntrada) {
		AvaliacaoDTO dtoSaida = comentarioService.avaliarComentario(codComentario, dtoEntrada);		
		HttpHeaders headers = configuraHeaderLocation(dtoSaida.getCodigo(), "/v1/avaliacoes");		
		return new ResponseEntity<>(dtoSaida, headers, CREATED);	
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
