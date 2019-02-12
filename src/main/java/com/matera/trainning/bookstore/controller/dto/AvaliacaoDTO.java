package com.matera.trainning.bookstore.controller.dto;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class AvaliacaoDTO {

	@Getter @Setter @JsonView
	private String avaliadoDescricao;
	
	@Min(0) @Max(5)
	@Column(name = "valor", nullable = false)
	private Integer valor;

}
