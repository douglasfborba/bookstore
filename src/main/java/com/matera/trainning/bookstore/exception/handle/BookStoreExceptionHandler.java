package com.matera.trainning.bookstore.exception.handle;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequestUri;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.matera.trainning.bookstore.exception.ResourceAlreadyExistsException;

@ControllerAdvice
public class BookStoreExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Object> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
    	HttpHeaders headers = new HttpHeaders();
		headers.setLocation(getUriDadoCodigoRecurso(ex.getCodigo()));
		return new ResponseEntity<Object>(headers, CONFLICT);
	}
	
	private URI getUriDadoCodigoRecurso(String codigo) {
		return fromCurrentRequestUri().path("/{codigo}").buildAndExpand(codigo).toUri();
	}
}
