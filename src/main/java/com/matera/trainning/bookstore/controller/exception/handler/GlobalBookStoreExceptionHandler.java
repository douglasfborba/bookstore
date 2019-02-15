package com.matera.trainning.bookstore.controller.exception.handler;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.matera.trainning.bookstore.service.exception.RecursoAlreadyExistsException;

@ControllerAdvice
public class GlobalBookStoreExceptionHandler {

	@ResponseBody
	@ExceptionHandler(RecursoAlreadyExistsException.class)
	public ResponseEntity<Object> handleResourceAlreadyExistsException(RecursoAlreadyExistsException ex) {
		HttpHeaders headers = configuraHeaderLocation(ex.getCodigo(), ex.getUriLocation());
		return new ResponseEntity<>(headers, CONFLICT);
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
