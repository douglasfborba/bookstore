package com.matera.trainning.bookstore.controller.dto;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import com.fasterxml.jackson.annotation.JsonView;
import com.matera.trainning.bookstore.model.Avaliacao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class AvaliacaoDTO {

	@Getter @Setter	@JsonView
	private String codigo;
	
	@Getter @Setter @JsonView
	private String descricao;
	
	@Getter @Setter
	@NotNull(message = "Campo usuário não pode ser nulo")
	@Size(min = 3, max = 50, message = "Campo usuário deve possuir entre 3 e 50 caracteres")
	private String usuario;
	
	@Getter @Setter
	@Min(0) @Max(5)
	@Column(name = "rating", nullable = false)
	@NotNull(message = "Campo rating não pode ser nulo")
	private Double rating;

	public static final Converter<Avaliacao, AvaliacaoDTO> getConverter() {
		Converter<Avaliacao, AvaliacaoDTO> converter = new Converter<Avaliacao, AvaliacaoDTO>() {
			
			@Override
			public AvaliacaoDTO convert(MappingContext<Avaliacao, AvaliacaoDTO> contexto) {
					Avaliacao avaliacao = contexto.getSource();

					AvaliacaoDTO dtoAvaliacao = new AvaliacaoDTO();
					dtoAvaliacao.setCodigo(avaliacao.getCodigo());
					dtoAvaliacao.setUsuario(avaliacao.getUsuario());
					dtoAvaliacao.setRating(avaliacao.getRating());

					if (avaliacao.getProduto() != null)
						dtoAvaliacao.setDescricao(avaliacao.getProduto().getDescricao());
					
					if (avaliacao.getComentario() != null)
						dtoAvaliacao.setDescricao(avaliacao.getComentario().getDescricao());

					return dtoAvaliacao;
				};
		};
		
		return converter;
	}
}
