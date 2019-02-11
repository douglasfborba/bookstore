package com.matera.trainning.bookstore.exception;

public class ResourceAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -1758818662225463733L;

	private String codigo;
		
	public ResourceAlreadyExistsException() {
		super("Recurso duplicado");
	}

	public ResourceAlreadyExistsException(String codigo) {
		super("Recurso " + codigo + " duplicado");
		this.codigo = codigo;
	}

	public ResourceAlreadyExistsException(Throwable causa) {
		super(causa);
	}

	public ResourceAlreadyExistsException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}

	public String getCodigo() {
		return codigo;
	}

}
