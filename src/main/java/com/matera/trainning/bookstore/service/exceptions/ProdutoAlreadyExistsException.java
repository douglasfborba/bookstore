package com.matera.trainning.bookstore.service.exceptions;

@SuppressWarnings("serial")
public class ProdutoAlreadyExistsException extends Exception {
	
	public ProdutoAlreadyExistsException() {
		super();
	}

	public ProdutoAlreadyExistsException(String mensagem) {
		super(mensagem);
	}

	public ProdutoAlreadyExistsException(Throwable causa) {
		super(causa);
	}

	public ProdutoAlreadyExistsException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
	
}
