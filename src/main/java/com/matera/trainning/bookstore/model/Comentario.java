package com.matera.trainning.bookstore.model;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "dis_comentario")
@JsonIgnoreProperties(value = { "id" })
public class Comentario implements Comparable<Comentario> {

	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "dis_cmtr_sequence")
	@SequenceGenerator(name = "dis_cmtr_sequence", sequenceName = "dis_cmtr_seq", allocationSize = 1)
	private Long id;

	@EqualsAndHashCode.Exclude
	private String codigo;
	
	@EqualsAndHashCode.Exclude
	@NotNull(message = "Campo descrição não pode ser nulo")
	@Size(min = 3, max = 250, message = "Campo descrição deve possuir entre 3 e 250 caracteres")
	private String descricao;

	@EqualsAndHashCode.Exclude
	@NotNull(message = "Campo usuário não pode ser nulo")
	@Size(min = 3, max = 50, message = "Campo usuário deve possuir entre 3 e 50 caracteres")
	private String usuario;

	@EqualsAndHashCode.Exclude
	@JsonFormat(pattern = "dd-MM-yyyy@HH:mm")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime dataHoraCriacao;

	@EqualsAndHashCode.Exclude
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "produto_id", nullable = false)
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "codigo")
	@JsonIdentityReference(alwaysAsId = true)
	@JsonProperty("codigoProduto")
	private Produto produto;

	public Comentario(String descricao, String codigo, String usuario, LocalDateTime dataHoraCriacao, Produto produto) {
		this.descricao = descricao;
		this.codigo = codigo;
		this.usuario = usuario;
		this.dataHoraCriacao = dataHoraCriacao;
		this.produto = produto;
	}

	@Override
	public int compareTo(Comentario outroComentario) {
		return this.getDataHoraCriacao().compareTo(outroComentario.getDataHoraCriacao());
	}

}
