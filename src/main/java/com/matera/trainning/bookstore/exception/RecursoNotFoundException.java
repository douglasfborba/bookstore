package com.matera.trainning.bookstore.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = NOT_FOUND)
public class RecursoNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -241024142683980912L;

	private String codigo;
	
	public RecursoNotFoundException() {
		super("Recurso inexistente");
	}

	public RecursoNotFoundException(String codigo) {
		super("Recurso " + codigo + " inexistente");
		this.codigo = codigo;
	}

	public RecursoNotFoundException(Throwable causa) {
		super(causa);
	}

	public RecursoNotFoundException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
	
	public String getCodigo() {
		return codigo;
	}

}
