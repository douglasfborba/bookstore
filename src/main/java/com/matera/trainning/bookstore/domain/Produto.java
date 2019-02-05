package com.matera.trainning.bookstore.domain;

import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.GenerationType.SEQUENCE;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dis_produto")

@Data
@NoArgsConstructor
public class Produto {
	
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = SEQUENCE, generator = "dis_prod_sequence")
	@SequenceGenerator(name = "dis_prod_sequence", sequenceName = "dis_prod_seq", allocationSize = 1)
	private Long id;

	@EqualsAndHashCode.Exclude
	@Column(name = "codigo", nullable = false)
	private String codigo;

	@EqualsAndHashCode.Exclude
	@Column(name = "descricao", nullable = false)
	private String descricao;

	@EqualsAndHashCode.Exclude
	@Column(name = "preco", nullable = false)
	private BigDecimal preco;

	@EqualsAndHashCode.Exclude
	@Column(name = "dataCadastro", nullable = false)
	private LocalDate dataCadastro;
	
	@OneToMany(mappedBy = "produto", cascade = REMOVE, orphanRemoval = true)
	private Set<Comentario> comentarios;
	
	@OneToMany(mappedBy = "produto", cascade = REMOVE, orphanRemoval = true)
	private Set<HistoricoDePreco> precos;

	public Produto(String codigo, String descricao, BigDecimal preco, LocalDate dataCadastro) {
		this.codigo = codigo;
		this.descricao = descricao;
		this.preco = preco;
		this.dataCadastro = dataCadastro;
	}
	
}
