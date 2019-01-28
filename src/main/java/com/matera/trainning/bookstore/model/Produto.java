package com.matera.trainning.bookstore.model;

import static javax.persistence.GenerationType.SEQUENCE;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "dis_produto")
@JsonIgnoreProperties(value = { "id" })
public class Produto {

	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "dis_prod_sequence")
	@SequenceGenerator(name = "dis_prod_sequence", sequenceName = "dis_prod_seq")
	private Long id;

	@EqualsAndHashCode.Exclude
	private String codigo;

	@EqualsAndHashCode.Exclude
	private String descricao;

	@EqualsAndHashCode.Exclude
	private BigDecimal preco;

	@EqualsAndHashCode.Exclude
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dataCadastro;

	public Produto(String codigo, String descricao, BigDecimal preco, LocalDate dataCadastro) {
		this.codigo = codigo;
		this.descricao = descricao;
		this.preco = preco;
		this.dataCadastro = dataCadastro;
	}

}
