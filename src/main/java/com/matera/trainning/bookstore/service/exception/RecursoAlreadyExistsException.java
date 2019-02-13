package com.matera.trainning.bookstore.service.exception;

public class RecursoAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -1758818662225463733L;

	private String codigo;
		
	public RecursoAlreadyExistsException() {
		super("Recurso duplicado");
	}

	public RecursoAlreadyExistsException(String codigo) {
		super("Recurso " + codigo + " duplicado");
		this.codigo = codigo;
	}

	public RecursoAlreadyExistsException(Throwable causa) {
		super(causa);
	}

	public RecursoAlreadyExistsException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}

	public String getCodigo() {
		return codigo;
	}

}
