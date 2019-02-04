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

import com.matera.trainning.bookstore.controller.dto.ProdutoDTO;
import com.matera.trainning.bookstore.controller.facade.ProdutoFacade;
import com.matera.trainning.bookstore.service.exceptions.RegistroAlreadyExistsException;
import com.matera.trainning.bookstore.service.exceptions.RegistroNotFoundException;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

	@Autowired
	private ProdutoFacade facade;

	@PostMapping
	public ResponseEntity<ProdutoDTO> insert(@Valid @RequestBody ProdutoDTO produtoDto, HttpServletResponse response) {
		try {
			ProdutoDTO dto = facade.insert(produtoDto);				
			URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
						.path("/{codigo}").buildAndExpand(dto.getCodigo()).toUri();			
			return ResponseEntity.created(uri).body(dto);
		} catch (RegistroAlreadyExistsException ex) {
			throw new ResponseStatusException(CONFLICT, ex.getMessage(), ex);
		}
	}

	@PutMapping("/{codigo}")
	@ResponseStatus(code = NO_CONTENT)
	public void update(@PathVariable String codigo, @Valid @RequestBody ProdutoDTO produtoDto, HttpServletResponse response) {
		try {
			facade.update(codigo, produtoDto);
		} catch (RegistroNotFoundException ex) {
			throw new ResponseStatusException(NOT_FOUND, ex.getMessage(), ex);
		} catch (RegistroAlreadyExistsException ex) {
			throw new ResponseStatusException(CONFLICT, ex.getMessage(), ex);
		}
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(code = NO_CONTENT)
	public void delete(@PathVariable String codigo) {
		try {
			facade.delete(codigo);
		} catch (RegistroNotFoundException ex) {
			throw new ResponseStatusException(NOT_FOUND, ex.getMessage(), ex);
		}
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<ProdutoDTO> findByCodigo(@PathVariable String codigo) {
		try {
			ProdutoDTO dto = facade.findByCodigo(codigo);
			return ResponseEntity.ok(dto);
		} catch (RegistroNotFoundException ex) {
			throw new ResponseStatusException(NOT_FOUND, ex.getMessage(), ex);
		}
	}

	@GetMapping("/search")
	public List<ProdutoDTO> findByDescricao(@RequestParam("descricao") String descricao) {
		return facade.findByDescricao(descricao);
	}

	@GetMapping
	public List<ProdutoDTO> findAll() {
		return facade.findAll();
	}

}
