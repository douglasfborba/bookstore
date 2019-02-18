package com.matera.trainning.bookstore.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

@Entity
@Table(name = "dis_comentario")
public class Comentario {

	@EqualsAndHashCode.Exclude
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = SEQUENCE, generator = "dis_cmtr_sequence")
	@SequenceGenerator(name = "dis_cmtr_sequence", sequenceName = "dis_cmtr_seq", allocationSize = 1)
	private Long id;

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
	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "produto_id", nullable = false)
	private Produto produto;
	
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "comentario", fetch = LAZY, cascade = ALL)
	private Set<Avaliacao> avaliacoes = new HashSet<>();
		
	public void addAvaliacao(Avaliacao avaliacao) {
		this.avaliacoes.add(avaliacao);
	}
	
	public void removeAvaliacao(Avaliacao avaliacao) {
		this.avaliacoes.remove(avaliacao);
	}

}
