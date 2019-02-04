package com.matera.trainning.bookstore.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable

@Data
@NoArgsConstructor
public class HistoricoPK implements Serializable {

	private static final long serialVersionUID = -3544478716843638749L;

	@ManyToOne
	@JoinColumn(name = "produto_id", referencedColumnName = "id", nullable = false)
	private Produto produto;

	@Column(name = "data_alteracao", nullable = false)
	private LocalDateTime dataHoraAlteracao;

	public HistoricoPK(Produto produto, LocalDateTime dataHoraAlteracao) {
		this.produto = produto;
		this.dataHoraAlteracao = dataHoraAlteracao;
	}

}
