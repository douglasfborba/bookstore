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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel(value = "Comentario", description = "Representa uma comentário")
@NoArgsConstructor
public class ComentarioDTO {

    @ApiModelProperty(notes = "Código alfanumérico que identifica o comentário", dataType = "java.lang.String", required = true)
	@Getter @Setter	@JsonView
	private String codigo;
	
    @ApiModelProperty(notes = "Conteúdo do comentário", dataType = "java.lang.String", required = true)
	@Getter @Setter
	@EqualsAndHashCode.Exclude
	@NotNull(message = "Campo descrição não pode ser nulo")
	@Size(min = 3, max = 250, message = "Campo descrição deve possuir entre 3 e 250 caracteres")
	private String descricao;
	
    @ApiModelProperty(notes = "Descrição do comentário", dataType = "java.lang.String", required = true)
	@Getter @Setter
	@EqualsAndHashCode.Exclude
	@NotNull(message = "Campo usuário não pode ser nulo")
	@Size(min = 3, max = 50, message = "Campo usuário deve possuir entre 3 e 50 caracteres")
	private String usuario;
	
    @ApiModelProperty(notes = "Descrição do comentário", required = true)
	@Getter @Setter
	@EqualsAndHashCode.Exclude
    @JsonView @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime dataHoraCriacao;
	
    @ApiModelProperty(notes = "Descrição do comentário", dataType = "java.lang.Double", required = true)
	@Getter @Setter @JsonView
	@EqualsAndHashCode.Exclude
	private Double rating;
 
}
