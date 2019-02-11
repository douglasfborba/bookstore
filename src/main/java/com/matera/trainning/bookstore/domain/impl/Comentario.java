package com.matera.trainning.bookstore.domain.impl;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.matera.trainning.bookstore.domain.Avaliado;

@Entity
@Table(name = "dis_comentario")
public class Comentario extends Avaliado {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = SEQUENCE, generator = "dis_cmtr_sequence")
	@SequenceGenerator(name = "dis_cmtr_sequence", sequenceName = "dis_cmtr_seq", allocationSize = 1)
	private Long id;

	@Column(name = "usuario", nullable = false)
	private String usuario;

	@Column(name = "data_hora_criacao", nullable = false)
	private LocalDateTime dataHoraCriacao;

	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "produto_id", nullable = false)
	private Produto produto;

	public Comentario() { }
	
	public Comentario(String descricao, String codigo, String usuario, LocalDateTime dataHoraCriacao, Produto produto) {
		super.setCodigo(codigo);
		super.setDescricao(descricao);
		this.usuario = usuario;
		this.dataHoraCriacao = dataHoraCriacao;
		this.produto = produto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public LocalDateTime getDataHoraCriacao() {
		return dataHoraCriacao;
	}

	public void setDataHoraCriacao(LocalDateTime dataHoraCriacao) {
		this.dataHoraCriacao = dataHoraCriacao;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

}
