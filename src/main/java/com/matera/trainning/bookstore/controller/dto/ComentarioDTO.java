package com.matera.trainning.bookstore.controller.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class ComentarioDTO {

	@Getter @Setter	@JsonView
	private String codigo;
	
	@Getter @Setter
	@EqualsAndHashCode.Exclude
	@NotNull(message = "Campo descrição não pode ser nulo")
	@Size(min = 3, max = 250, message = "Campo descrição deve possuir entre 3 e 250 caracteres")
	private String descricao;
	
	@Getter @Setter
	@EqualsAndHashCode.Exclude
	@NotNull(message = "Campo usuário não pode ser nulo")
	@Size(min = 3, max = 50, message = "Campo usuário deve possuir entre 3 e 50 caracteres")
	private String usuario;
	
	@Getter @Setter
	@EqualsAndHashCode.Exclude
    @JsonView @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime dataHoraCriacao;
	
	@Getter @Setter @JsonView
	@EqualsAndHashCode.Exclude
	private Double rating;
		    
}
