package com.matera.trainning.bookstore.controller;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(description = "Comentário APIs", tags = "Comentário")
@ApiResponses(value = {
        @ApiResponse(code = 401, message = "Erro de autenticação")
})
@RestController
@RequestMapping
public class ComentarioController {
	
	@Autowired
	private ComentarioService comentarioService;
	
	@ApiOperation(value = "Atualiza comentário", notes = "Sem retorno")
	@ApiResponses(value = {
	        @ApiResponse(code = 204, message = "Recurso atualizado"),
	        @ApiResponse(code = 404, message = "Recurso inexistente")
	})
	@PutMapping(value = "v1/comentarios/{codComentario}", produces = "application/json")
	@ResponseStatus(code = NO_CONTENT)
	public void atualizarComentario(@ApiParam(value = "Código do comentário") @PathVariable String codComentario, 
			@ApiParam(value = "JSON com os dados com comentário") @Valid @RequestBody ComentarioDTO dtoEntrada) {
		comentarioService.atualizarComentario(codComentario, dtoEntrada);
	}
	
	@ApiOperation(value = "Remove comentário", notes = "Sem retorno")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Recurso removido"),
	        @ApiResponse(code = 404, message = "Recurso inexistente")
	})
	@DeleteMapping(value = "v1/comentarios/{codComentario}", produces = "application/json")
	@ResponseStatus(code = NO_CONTENT)
	public void removerComentario(@ApiParam(value = "Código do comentário") @PathVariable String codComentario) {
		comentarioService.removerComentario(codComentario);
	}

	@ApiOperation(value = "Busca comentário dado código", notes = "Retorna o comentário buscado", response = ComentarioDTO.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Recurso encontrado"),
	        @ApiResponse(code = 404, message = "Recurso inexistente")
	})
	@GetMapping(value = "v1/comentarios/{codComentario}", produces = "application/json")
	public ResponseEntity<ComentarioDTO> buscarComentarioDadoCodigo(@ApiParam(value = "Código do comentário") @PathVariable String codComentario) {
		ComentarioDTO dtoSaida = comentarioService.buscarComentarioDadoCodigo(codComentario);
		return ResponseEntity.ok(dtoSaida);	
	}
	
	@ApiOperation(value = "Lista comentários dado usuário", notes = "Retorna uma lista de comentários")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "offset", dataType = "integer", paramType = "query", value = "Deslocamento a ser obtido de acordo com a página e o tamanho das páginas subjacentes"),
		@ApiImplicitParam(name = "pageNumber", dataType = "integer", paramType = "query", value = "Número da página retornada"),
	    @ApiImplicitParam(name = "pageSize", dataType = "integer", paramType = "query", value = "Número máximo de itens por página"),
	    @ApiImplicitParam(name = "paged", dataType = "boolean", paramType = "query", value = "Define se os registros serão paginados"),
	    @ApiImplicitParam(name = "sort.sorted", dataType = "boolean", paramType = "query", value = "Define se os registros serão ordenados"),
	    @ApiImplicitParam(name = "sort.unsorted", dataType = "boolean", paramType = "query", value = "Define se os registros não serão ordenados"),
	    @ApiImplicitParam(name = "unpaged", dataType = "boolean", paramType = "query", value = "Define se os registros não serão paginados"),
	    @ApiImplicitParam(name = "usuario", dataType = "string", paramType = "query", value = "Username do usuário")
	})
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Recurso encontrado"),
	        @ApiResponse(code = 404, message = "Recurso inexistente")
	})
	@GetMapping(value = "v1/comentarios", params = { "usuario" }, produces = "application/json")
	public Page<ComentarioDTO> listarComentariosDadoUsuario(@RequestParam(name = "usuario", required = true) String usuComentario,
			@PageableDefault(sort = "dataHoraCriacao", direction = DESC) Pageable pageable) {
		return comentarioService.listarComentariosDadoUsuario(usuComentario, pageable);
	}
	
	@ApiOperation(value = "Lista comentários com rating maior que valor passado como parâmetro", notes = "Retorna uma lista de comentários")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "offset", dataType = "integer", paramType = "query", value = "Deslocamento a ser obtido de acordo com a página e o tamanho das páginas subjacentes"),
		@ApiImplicitParam(name = "pageNumber", dataType = "integer", paramType = "query", value = "Número da página retornada"),
	    @ApiImplicitParam(name = "pageSize", dataType = "integer", paramType = "query", value = "Número máximo de itens por página"),
	    @ApiImplicitParam(name = "paged", dataType = "boolean", paramType = "query", value = "Define se os registros serão paginados"),
	    @ApiImplicitParam(name = "sort.sorted", dataType = "boolean", paramType = "query", value = "Define se os registros serão ordenados"),
	    @ApiImplicitParam(name = "sort.unsorted", dataType = "boolean", paramType = "query", value = "Define se os registros não serão ordenados"),
	    @ApiImplicitParam(name = "unpaged", dataType = "boolean", paramType = "query", value = "Define se os registros não serão paginados"),
	    @ApiImplicitParam(name = "gt", dataType = "integer", paramType = "query", value = "Rating do comentário")
	})
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Recurso encontrado"),
	        @ApiResponse(code = 404, message = "Recurso inexistente")
	})
	@GetMapping(value = "v1/comentarios/rating", params = { "gt" }, produces = "application/json")
	public Page<ComentarioDTO> listarComentariosComRatingMaiorQueParam(@RequestParam(name = "gt", required = true) Double rating, 
			@PageableDefault(sort = "rating", direction = DESC) Pageable pageable) {
		return comentarioService.listarComentariosComRatingMaiorQueParam(rating, pageable);
	}
		
	@ApiOperation(value = "Lista avaliações dado código do comentário", notes = "Retorna uma lista de avaliações")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "offset", dataType = "integer", paramType = "query", value = "Deslocamento a ser obtido de acordo com a página e o tamanho das páginas subjacentes"),
		@ApiImplicitParam(name = "pageNumber", dataType = "integer", paramType = "query", value = "Número da página retornada"),
	    @ApiImplicitParam(name = "pageSize", dataType = "integer", paramType = "query", value = "Número máximo de itens por página"),
	    @ApiImplicitParam(name = "paged", dataType = "boolean", paramType = "query", value = "Define se o Pageable corrente usará paginação"),
	    @ApiImplicitParam(name = "sort.sorted", dataType = "boolean", paramType = "query", value = "Define se os registros serão ordenados"),
	    @ApiImplicitParam(name = "sort.unsorted", dataType = "boolean", paramType = "query", value = "Define se os registros não serão ordenados"),
	    @ApiImplicitParam(name = "unpaged", dataType = "boolean", paramType = "query", value = "Define se os registros serão paginados"),
	})
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Recurso encontrado"),
	        @ApiResponse(code = 404, message = "Recurso inexistente")
	})
	@GetMapping(value = "v1/comentarios/{codComentario}/avaliacoes", produces = "application/json")
	public Page<AvaliacaoDTO> listarAvaliacoesDadoComentario(@ApiParam(value = "Código do comentário") @PathVariable String codComentario, 
			@PageableDefault(sort = "dataHoraCriacao", direction = DESC) Pageable pageable) {
		return comentarioService.listarAvaliacoesDadoCodigoComentario(codComentario, pageable);
	}

	@ApiOperation(value = "Avalia comentário", notes = "Retorna a avaliação criada", response = AvaliacaoDTO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Recurso criado"),
	        @ApiResponse(code = 404, message = "Recurso inexistente"),
		    @ApiResponse(code = 409, message = "Recurso duplicado")
	})
	@PostMapping(value = "v1/comentarios/{codComentario}/avaliacoes", produces = "application/json")
	@ResponseStatus(code = CREATED)
	public ResponseEntity<AvaliacaoDTO> avaliarComentario(@ApiParam(value = "Código do comentário") @PathVariable String codComentario,
			@ApiParam(value = "JSON com os dados com comentário") @Valid @RequestBody AvaliacaoDTO dtoEntrada) {
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
