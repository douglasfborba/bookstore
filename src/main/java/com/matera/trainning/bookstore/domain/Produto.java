package com.matera.trainning.bookstore.domain;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
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

@Data
@Entity
@Table(name = "dis_produto")
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

	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "produto", fetch = LAZY, cascade = ALL, orphanRemoval = true)
	private Set<Comentario> comentarios = new HashSet<>();

	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "produto", fetch = LAZY, cascade = ALL, orphanRemoval = true)
	private Set<HistoricoDePreco> precos = new HashSet<>();

	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "produto", fetch = LAZY, cascade = ALL)
	private Set<Avaliacao> avaliacoes = new HashSet<>();
	
	public Produto() { }
	
	public Produto(String codigo, String descricao, BigDecimal preco, LocalDate dataCadastro) {
		this.codigo = codigo;
		this.descricao = descricao;
		this.preco = preco;
		this.dataCadastro = dataCadastro;
	}
	
	public void addComentario(Comentario comentario) {
		this.comentarios.add(comentario);
	}

	public void removeComentario(Comentario comentario) {
		this.comentarios.remove(comentario);
	}
	
	public void addHistoricoDePreco(HistoricoDePreco itemHistorico) {
		this.precos.add(itemHistorico);
	}
	
	public void removeHistoricoDePreco(HistoricoDePreco itemHistorico) {
		this.precos.remove(itemHistorico);
	}
	
	public void addAvaliacao(Avaliacao avaliacao) {
		this.avaliacoes.add(avaliacao);
	}
	
	public void removeHistoricoDePreco(Avaliacao avaliacao) {
		this.avaliacoes.remove(avaliacao);
	}	

}
