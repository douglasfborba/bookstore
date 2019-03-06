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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel(value = "Preco", description = "Representa uma preço")
@NoArgsConstructor
public class HistoricoDePrecoDTO {
	
	@Getter @Setter @JsonView 
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime dataHoraAlteracao;
	
	@Getter @Setter @JsonView
	private String descricao;
	
	@Getter @Setter @JsonView
	private BigDecimal preco;

}
