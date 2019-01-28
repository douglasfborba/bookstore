package com.matera.trainning.bookstore.service.exceptions;

@SuppressWarnings("serial")
public class ProdutoNotFoundException extends Exception {

	public ProdutoNotFoundException() {
		super();
	}

	public ProdutoNotFoundException(String mensagem) {
		super(mensagem);
	}

	public ProdutoNotFoundException(Throwable causa) {
		super(causa);
	}

	public ProdutoNotFoundException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
	
}
