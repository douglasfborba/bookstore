package com.matera.trainning.bookstore.domain.impl;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.matera.trainning.bookstore.domain.Avaliado;

import lombok.Data;

@Data
@Entity
@DiscriminatorValue("COM")
@Table(name = "dis_comentario")
public class Comentario implements Avaliado {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = SEQUENCE, generator = "dis_cmtr_sequence")
	@SequenceGenerator(name = "dis_cmtr_sequence", sequenceName = "dis_cmtr_seq", allocationSize = 1)
	private Long id;

	@Column(name = "codigo", nullable = false)
	private String codigo;

	@Column(name = "descricao", nullable = false)
	private String descricao;
	
	@Column(name = "usuario", nullable = false)
	private String usuario;

	@Column(name = "data_hora_criacao", nullable = false)
	private LocalDateTime dataHoraCriacao;

	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "produto_id", nullable = false)
	private Produto produto;

	@Override
	public void setCodigo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDescricao() {
		// TODO Auto-generated method stub
		
	}

}
