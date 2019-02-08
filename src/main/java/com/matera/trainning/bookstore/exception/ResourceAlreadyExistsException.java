package com.matera.trainning.bookstore.exception;

import static org.springframework.http.HttpStatus.CONFLICT;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = CONFLICT)
public class ResourceAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -1758818662225463733L;

	public ResourceAlreadyExistsException() {
		super("Recurso duplicado");
	}

	public ResourceAlreadyExistsException(String mensagem) {
		super(mensagem);
	}

	public ResourceAlreadyExistsException(Throwable causa) {
		super(causa);
	}

	public ResourceAlreadyExistsException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}

}
