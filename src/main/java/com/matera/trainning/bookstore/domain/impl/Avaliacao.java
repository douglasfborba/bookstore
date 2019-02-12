package com.matera.trainning.bookstore.domain.impl;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.hibernate.annotations.CascadeType.ALL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.MetaValue;

import com.matera.trainning.bookstore.domain.Avaliado;

import lombok.Data;

@Data
@Entity
@Table(name = "dis_avaliacao")
public class Avaliacao {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = SEQUENCE, generator = "dis_aval_sequence")
	@SequenceGenerator(name = "dis_aval_sequence", sequenceName = "dis_aval_seq", allocationSize = 1)
	private Long id;

	@Any(metaColumn = @Column(name = "avaliado_tipo"))
	@AnyMetaDef(idType = "long", metaType = "string", metaValues = {
			@MetaValue(targetEntity = Produto.class, value = "PROD"),
			@MetaValue(targetEntity = Comentario.class, value = "COM") })
	@Cascade({ ALL })
	@JoinColumn(name = "avaliado_id")
	private Avaliado avaliado;
	
	@Column(name = "valor", nullable = false)
	private Integer valor;

}
