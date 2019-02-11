package com.matera.trainning.bookstore.domain.impl;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.matera.trainning.bookstore.domain.Avaliado;

@Entity
public class Avaliacao {

	@Id
	private Long id;
	private Avaliado avaliado;
	private Integer valor;

	public Avaliacao() {	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Avaliado getAvaliado() {
		return avaliado;
	}

	public void setAvaliado(Avaliado avaliado) {
		this.avaliado = avaliado;
	}

	public Integer getValor() {
		return valor;
	}

	public void setValor(Integer valor) {
		this.valor = valor;
	}

}
