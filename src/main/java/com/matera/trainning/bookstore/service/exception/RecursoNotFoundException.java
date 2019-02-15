package com.matera.trainning.bookstore.service.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = NOT_FOUND)
public class RecursoNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -241024142683980912L;

	private String codigo;
	private String uriLocation;

	public RecursoNotFoundException() {
		super("Recurso inexistente");
	}

	public RecursoNotFoundException(String mensagem) {
		super(mensagem);
	}

	public RecursoNotFoundException(String mensagem, String codigo, String uriLocation) {
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
