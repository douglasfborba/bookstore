package com.matera.trainning.bookstore.service.exceptions;

@SuppressWarnings("serial")
public class RegistroNotFoundException extends Exception {

	public RegistroNotFoundException() {
		super();
	}

	public RegistroNotFoundException(String mensagem) {
		super(mensagem);
	}

	public RegistroNotFoundException(Throwable causa) {
		super(causa);
	}

	public RegistroNotFoundException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
	
}
