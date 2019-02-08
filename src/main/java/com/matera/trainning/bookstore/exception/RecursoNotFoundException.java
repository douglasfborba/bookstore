package com.matera.trainning.bookstore.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = NOT_FOUND)
public class RecursoNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -241024142683980912L;

	public RecursoNotFoundException() {
		super("Recurso inexistente");
	}

	public RecursoNotFoundException(String mensagem) {
		super(mensagem);
	}

	public RecursoNotFoundException(Throwable causa) {
		super(causa);
	}

	public RecursoNotFoundException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}

}
