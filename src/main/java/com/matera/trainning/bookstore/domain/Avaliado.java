package com.matera.trainning.bookstore.domain;

import javax.persistence.Column;

public abstract class Avaliado {

	@Column(name = "codigo", nullable = false)
	private String codigo;

	@Column(name = "descricao", nullable = false)
	private String descricao;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
