package com.matera.trainning.bookstore.domain.impl;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.matera.trainning.bookstore.domain.Avaliado;

import lombok.Data;

@Data
@Entity
@DiscriminatorValue("PROD")
@Table(name = "dis_produto")
public class Produto implements Avaliado {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = SEQUENCE, generator = "dis_prod_sequence")
	@SequenceGenerator(name = "dis_prod_sequence", sequenceName = "dis_prod_seq", allocationSize = 1)
	private Long id;

	@Column(name = "codigo", nullable = false)
	private String codigo;

	@Column(name = "descricao", nullable = false)
	private String descricao;

	@Column(name = "preco", nullable = false)
	private BigDecimal preco;

	@Column(name = "dataCadastro", nullable = false)
	private LocalDate dataCadastro;

	@OneToMany(mappedBy = "produto", fetch = LAZY, cascade = ALL, orphanRemoval = true)
	private Set<Comentario> comentarios = new HashSet<>();

	@OneToMany(mappedBy = "produto", fetch = LAZY, cascade = ALL, orphanRemoval = true)
	private Set<HistoricoDePreco> precos = new HashSet<>();

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

	@Override
	public void setCodigo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDescricao() {
		// TODO Auto-generated method stub
		
	}

}
