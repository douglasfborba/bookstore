package com.matera.trainning.bookstore.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel(value = "Preco", description = "Representa uma preço")
@NoArgsConstructor
public class HistoricoDePrecoDTO {
	
    @ApiModelProperty(notes = "Data e hora de criação do preço", required = true)
	@Getter @Setter @JsonView 
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime dataHoraAlteracao;
	
    @ApiModelProperty(notes = "Descrição do produto", dataType = "java.util.String", required = true)
	@Getter @Setter @JsonView
	private String descricao;
	
    @ApiModelProperty(notes = "Preço do produto", dataType = "java.math.BigDecimal", required = true)
	@Getter @Setter @JsonView
	private BigDecimal preco;

}
