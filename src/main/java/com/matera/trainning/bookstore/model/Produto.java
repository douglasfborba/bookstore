package com.matera.trainning.bookstore.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "dis_produto")
@JsonIgnoreProperties(value = { "id", "comentarios" })
public class Produto {

	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "dis_prod_sequence")
	@SequenceGenerator(name = "dis_prod_sequence", sequenceName = "dis_prod_seq", allocationSize = 1)
	private Long id;

	@EqualsAndHashCode.Exclude
	@NotNull(message = "Campo código não pode ser nulo")
	@Size(min = 1, max = 10, message = "Campo código deve possuir entre 1 e 10 caracteres")
	private String codigo;

	@EqualsAndHashCode.Exclude
	@NotNull(message = "Campo código não pode ser nulo")
	@Size(min = 3, max = 50, message = "Campo código deve possuir entre 3 e 50 caracteres")
	private String descricao;

	@EqualsAndHashCode.Exclude
	@NotNull(message = "Campo preço não pode ser nulo")
	private BigDecimal preco;

	@EqualsAndHashCode.Exclude
	@JsonFormat(pattern = "dd-MM-yyyy")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate dataCadastro;

	@EqualsAndHashCode.Exclude
	@OrderBy("dataHoraCriacao ASC")
	@OneToMany(mappedBy = "produto", cascade = ALL, fetch = LAZY)
	private Set<Comentario> comentarios;

	public Produto(String codigo, String descricao, BigDecimal preco, LocalDate dataCadastro) {
		this.codigo = codigo;
		this.descricao = descricao;
		this.preco = preco;
		this.dataCadastro = dataCadastro;
	}

}
