package com.matera.trainning.bookstore.model;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "dis_avaliacao")
public class Avaliacao {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = SEQUENCE, generator = "dis_aval_sequence")
	@SequenceGenerator(name = "dis_aval_sequence", sequenceName = "dis_aval_seq", allocationSize = 1)
	private Long id;

	@EqualsAndHashCode.Exclude
	@Column(name = "codigo", nullable = false)
	private String codigo;
	
	@EqualsAndHashCode.Exclude
	@Column(name = "usuario", nullable = false, unique = true)
	private String usuario;
	
	@EqualsAndHashCode.Exclude
	@ManyToOne(fetch = LAZY, optional = true)
	@JoinColumn(name = "produto_id", nullable = true)
	private Produto produto;

	@EqualsAndHashCode.Exclude
	@ManyToOne(fetch = LAZY, optional = true)
	@JoinColumn(name = "comentario_id", nullable = true)
	private Comentario comentario;
	
	@EqualsAndHashCode.Exclude
	@Column(name = "rating", nullable = false)
	private Double rating;
	
}
