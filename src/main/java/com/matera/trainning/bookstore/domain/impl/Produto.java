package com.matera.trainning.bookstore.domain.impl;

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

import com.matera.trainning.bookstore.domain.Avaliado;

@Entity
@Table(name = "dis_produto")
public class Produto extends Avaliado {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = SEQUENCE, generator = "dis_prod_sequence")
	@SequenceGenerator(name = "dis_prod_sequence", sequenceName = "dis_prod_seq", allocationSize = 1)
	private Long id;

	@Column(name = "preco", nullable = false)
	private BigDecimal preco;

	@Column(name = "dataCadastro", nullable = false)
	private LocalDate dataCadastro;

	@OneToMany(mappedBy = "produto", fetch = LAZY, cascade = ALL, orphanRemoval = true)
	private Set<Comentario> comentarios = new HashSet<Comentario>();

	@OneToMany(mappedBy = "produto", fetch = LAZY, cascade = ALL, orphanRemoval = true)
	private Set<HistoricoDePreco> precos = new HashSet<HistoricoDePreco>();

	public Produto() { }
	
	public Produto(String codigo, String descricao, BigDecimal preco, LocalDate dataCadastro) {
		super.setCodigo(codigo);
		super.setDescricao(descricao);
		this.preco = preco;
		this.dataCadastro = dataCadastro;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public LocalDate getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDate dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Set<Comentario> getComentarios() {
		return comentarios;
	}

	public void setComentarios(Set<Comentario> comentarios) {
		this.comentarios = comentarios;
	}

	public Set<HistoricoDePreco> getPrecos() {
		return precos;
	}

	public void setPrecos(Set<HistoricoDePreco> precos) {
		this.precos = precos;
	}

	public void addComentario(Comentario comentario) {
		this.comentarios.add(comentario);
	}

	public void removeComentario(Comentario comentario) {
		comentarios.remove(comentario);
	}

	public void addHistoricoDePreco(HistoricoDePreco preco) {
		this.precos.add(preco);
	}

	public void removeHistoricoDePreco(HistoricoDePreco preco) {
		precos.remove(preco);
	}

}
