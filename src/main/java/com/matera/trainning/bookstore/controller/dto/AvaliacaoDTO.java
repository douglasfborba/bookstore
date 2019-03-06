package com.matera.trainning.bookstore.controller.dto;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel(value = "Avaliacao", description = "Representa uma avaliação")
@NoArgsConstructor
public class AvaliacaoDTO {

    @ApiModelProperty(notes = "Código alfanumérico que identifica a avaliação", dataType = "java.lang.String", required = true)
	@Getter @Setter	@JsonView
	private String codigo;
	
    @ApiModelProperty(notes = "Descrição da avaliação", dataType = "java.lang.String", required = true)
	@Getter @Setter @JsonView
	@EqualsAndHashCode.Exclude
	private String descricao;
	
    @ApiModelProperty(notes = "Usuário que criou a avaliação", dataType = "java.lang.String", required = true)
	@Getter @Setter
	@EqualsAndHashCode.Exclude
	@NotNull(message = "Campo usuário não pode ser nulo")
	@Size(min = 3, max = 50, message = "Campo usuário deve possuir entre 3 e 50 caracteres")
	private String usuario;
	
    @ApiModelProperty(notes = "Rating da avaliação", dataType = "java.lang.Double", required = true)
	@Getter @Setter
	@EqualsAndHashCode.Exclude
	@Min(0) @Max(5)
	@Column(name = "rating", nullable = false)
	@NotNull(message = "Campo rating não pode ser nulo")
	private Double rating;

}
