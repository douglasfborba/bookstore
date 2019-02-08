package com.matera.trainning.bookstore.domain;

import static javax.persistence.GenerationType.SEQUENCE;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dis_historico")

@Data
@NoArgsConstructor
public class HistoricoDePreco {
	
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = SEQUENCE, generator = "dis_hist_sequence")
	@SequenceGenerator(name = "dis_hist_sequence", sequenceName = "dis_hist_seq", allocationSize = 1)
	private Long id;
	
	@EqualsAndHashCode.Exclude
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "produto_id", nullable = false)
	private Produto produto;

	@EqualsAndHashCode.Exclude
	@Column(name = "data_hora_alteracao", nullable = false)
	private LocalDateTime dataHoraAlteracao;
	
	@EqualsAndHashCode.Exclude
	@Column(name = "preco", nullable = false)
	private BigDecimal preco;

	public HistoricoDePreco(Produto produto, LocalDateTime dataHoraAlteracao, BigDecimal preco) {
		this.produto = produto;
		this.dataHoraAlteracao = dataHoraAlteracao;
		this.preco = preco;
	}
		
}



