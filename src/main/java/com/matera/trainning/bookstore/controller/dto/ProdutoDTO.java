package com.matera.trainning.bookstore.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.matera.trainning.bookstore.controller.validation.ValidaDescricaoAndPreco;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel(value = "Produto", description = "Representa um produto")
@NoArgsConstructor
@ValidaDescricaoAndPreco(baseField = "descricao", matchField = "preco", message = "Preço deve ser superior a 10.0")
public class ProdutoDTO {

    @ApiModelProperty(notes = "Código alfanumérico que identifica o produto", dataType = "java.util.String", required = true)
	@Getter @Setter
	@NotNull(message = "Campo código não pode ser nulo")
	@Size(min = 1, max = 10, message = "Campo código deve possuir entre 1 e 10 caracteres")
	private String codigo;

    @ApiModelProperty(notes = "Descrição do produto", dataType = "java.util.String", required = true)
	@Getter @Setter
	@EqualsAndHashCode.Exclude
	@NotNull(message = "Campo código não pode ser nulo")
	@Size(min = 3, max = 50, message = "Campo código deve possuir entre 3 e 50 caracteres")
	private String descricao;

    @ApiModelProperty(notes = "Preço do produto", dataType = "java.math.BigDecimal", required = true)
	@Getter @Setter
	@EqualsAndHashCode.Exclude
	@NotNull(message = "Campo preço não pode ser nulo")
	private BigDecimal preco;

    @ApiModelProperty(notes = "Data de criação do produto", required = true)
	@Getter @Setter
	@EqualsAndHashCode.Exclude
	@JsonView @JsonFormat(pattern = "dd-MM-yyyy")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate dataCadastro;
	
    @ApiModelProperty(notes = "Rating do produto", dataType = "java.lang.Double", required = true)
	@Getter @Setter @JsonView
	@EqualsAndHashCode.Exclude
	private Double rating;

}
