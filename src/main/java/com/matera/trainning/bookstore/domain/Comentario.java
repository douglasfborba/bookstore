package com.matera.trainning.bookstore.domain;

import static javax.persistence.GenerationType.SEQUENCE;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dis_comentario")

@Data
@NoArgsConstructor
public class Comentario {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = SEQUENCE, generator = "dis_cmtr_sequence")
	@SequenceGenerator(name = "dis_cmtr_sequence", sequenceName = "dis_cmtr_seq", allocationSize = 1)
	private Long id;

	@EqualsAndHashCode.Exclude
	@Column(name = "codigo", nullable = false)
	private String codigo;

	@EqualsAndHashCode.Exclude
	@Column(name = "descricao", nullable = false)
	private String descricao;

	@EqualsAndHashCode.Exclude
	@Column(name = "usuario", nullable = false)
	private String usuario;

	@EqualsAndHashCode.Exclude
	@Column(name = "data_hora_criacao", nullable = false)
	private LocalDateTime dataHoraCriacao;

	@EqualsAndHashCode.Exclude
	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "produto_id", nullable = false)
	private Produto produto;

	public Comentario(String descricao, String codigo, String usuario, LocalDateTime dataHoraCriacao, Produto produto) {
		this.descricao = descricao;
		this.codigo = codigo;
		this.usuario = usuario;
		this.dataHoraCriacao = dataHoraCriacao;
		this.produto = produto;
	}

}
