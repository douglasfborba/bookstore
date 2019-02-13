package com.matera.trainning.bookstore.exception.handle;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequestUri;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.matera.trainning.bookstore.exception.RecursoAlreadyExistsException;

@ControllerAdvice
public class BookStoreExceptionHandler {

    @ExceptionHandler(RecursoAlreadyExistsException.class)
    public ResponseEntity<Object> handleResourceAlreadyExistsException(RecursoAlreadyExistsException ex) {
    	HttpHeaders headers = new HttpHeaders();
		headers.setLocation(getUriDadoCodigoRecurso(ex.getCodigo()));
		return new ResponseEntity<Object>(headers, CONFLICT);
	}
	
	private URI getUriDadoCodigoRecurso(String codigo) {
		return fromCurrentRequestUri().path("/{codigo}").buildAndExpand(codigo).toUri();
	}
}
