package com.matera.trainning.bookstore.model;

import static javax.persistence.GenerationType.SEQUENCE;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "dis_produto")
public class Product {

	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "dis_prod_sequence")
	@SequenceGenerator(name = "dis_prod_sequence", sequenceName = "dis_prod_seq")
	private Long id;

	@EqualsAndHashCode.Exclude
	@Column(name = "codigo")
	private String code;

	@EqualsAndHashCode.Exclude
	@Column(name = "descricao")
	private String description;

	@EqualsAndHashCode.Exclude
	@Column(name = "preco")
	private BigDecimal price;

	@EqualsAndHashCode.Exclude
	@Column(name = "data_cadastro")
	private LocalDate creationDate;

}
