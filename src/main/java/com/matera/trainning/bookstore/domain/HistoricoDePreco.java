package com.matera.trainning.bookstore.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "dis_historico")

@Data
public class HistoricoDePreco implements Serializable {
	
	private static final long serialVersionUID = 4956821838720223133L;

	@EmbeddedId
	private HistoricoDePrecoPK pk;
	
	@EqualsAndHashCode.Exclude
	@Column(name = "preco", nullable = false)
	private BigDecimal preco;
		
}



