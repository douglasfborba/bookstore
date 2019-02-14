package com.matera.trainning.bookstore.controller.exception.handler;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequestUri;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.matera.trainning.bookstore.service.exception.RecursoAlreadyExistsException;

@ControllerAdvice
public class BookStoreExceptionHandler {

    @ExceptionHandler(RecursoAlreadyExistsException.class)
    public ResponseEntity<Object> handleResourceAlreadyExistsException(RecursoAlreadyExistsException ex) {
    	HttpHeaders headers = new HttpHeaders();
		headers.setLocation(getUriDadoCodigoRecurso(ex.getCodigo()));
		return new ResponseEntity<>(headers, CONFLICT);
	}
	
	private URI getUriDadoCodigoRecurso(String codigo) {
		return fromCurrentRequestUri().path("/{codigo}").buildAndExpand(codigo).toUri();
	}
	
}
