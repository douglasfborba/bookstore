package com.matera.trainning.bookstore.service.exceptions;

@SuppressWarnings("serial")
public class RegistroAlreadyExistsException extends Exception {
	
	public RegistroAlreadyExistsException() {
		super();
	}

	public RegistroAlreadyExistsException(String mensagem) {
		super(mensagem);
	}

	public RegistroAlreadyExistsException(Throwable causa) {
		super(causa);
	}

	public RegistroAlreadyExistsException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
	
}
