package com.matera.trainning.bookstore.service.exception;

public class RecursoAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -1758818662225463733L;

	private String codigo;
	private String uriLocation;

	public RecursoAlreadyExistsException() {
		super("Recurso duplicado");
	}

	public RecursoAlreadyExistsException(String mensagem) {
		super(mensagem);
	}

	public RecursoAlreadyExistsException(String mensagem, String codigo, String uriLocation) {
		super(mensagem);
		this.codigo = codigo;
		this.uriLocation = uriLocation;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getUriLocation() {
		return uriLocation;
	}

	public void setUriLocation(String uriLocation) {
		this.uriLocation = uriLocation;
	}

}
