package com.matera.trainning.bookstore.model;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

@Entity
@Table(name = "dis_historico")
public class HistoricoDePreco {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@EqualsAndHashCode.Exclude
	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "produto_id", nullable = false)
	private Produto produto;

	@EqualsAndHashCode.Exclude
	@Column(name = "data_hora_alteracao", nullable = false)
	private LocalDateTime dataHoraAlteracao;

	@EqualsAndHashCode.Exclude
	@Column(name = "preco", nullable = false)
	private BigDecimal preco;
	
}
