package com.matera.trainning.bookstore.domain.impl;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "dis_historico")
public class HistoricoDePreco {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = SEQUENCE, generator = "dis_hist_sequence")
	@SequenceGenerator(name = "dis_hist_sequence", sequenceName = "dis_hist_seq", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "produto_id", nullable = false)
	private Produto produto;

	@Column(name = "data_hora_alteracao", nullable = false)
	private LocalDateTime dataHoraAlteracao;

	@Column(name = "preco", nullable = false)
	private BigDecimal preco;

	public HistoricoDePreco() { }
	
	public HistoricoDePreco(Produto produto, LocalDateTime dataHoraAlteracao, BigDecimal preco) {
		this.produto = produto;
		this.dataHoraAlteracao = dataHoraAlteracao;
		this.preco = preco;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public LocalDateTime getDataHoraAlteracao() {
		return dataHoraAlteracao;
	}

	public void setDataHoraAlteracao(LocalDateTime dataHoraAlteracao) {
		this.dataHoraAlteracao = dataHoraAlteracao;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

}
