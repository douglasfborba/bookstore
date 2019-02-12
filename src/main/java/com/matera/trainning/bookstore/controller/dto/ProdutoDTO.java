package com.matera.trainning.bookstore.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.OptionalDouble;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.matera.trainning.bookstore.controller.validation.ValidaDescricaoAndPreco;
import com.matera.trainning.bookstore.domain.Produto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@ValidaDescricaoAndPreco(baseField = "descricao", matchField = "preco", message = "Preço deve ser superior a 10.0")
public class ProdutoDTO {

	@Getter @Setter
	@NotNull(message = "Campo código não pode ser nulo")
	@Size(min = 1, max = 10, message = "Campo código deve possuir entre 1 e 10 caracteres")
	private String codigo;

	@Getter @Setter
	@NotNull(message = "Campo código não pode ser nulo")
	@Size(min = 3, max = 50, message = "Campo código deve possuir entre 3 e 50 caracteres")
	private String descricao;

	@Getter @Setter
	@NotNull(message = "Campo preço não pode ser nulo")
	private BigDecimal preco;

	@Getter @Setter
	@JsonView @JsonFormat(pattern = "dd-MM-yyyy")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate dataCadastro;
	
	@Getter @Setter @JsonView
	private Double rating;

	public static final Converter<Produto, ProdutoDTO> getConverter() {
		Converter<Produto, ProdutoDTO> conversor = new Converter<Produto, ProdutoDTO>() {
			
			@Override
			public ProdutoDTO convert(MappingContext<Produto, ProdutoDTO> contexto) {
				Produto produto = contexto.getSource();
	
				ProdutoDTO dtoProduto = new ProdutoDTO();				
				dtoProduto.setCodigo(produto.getCodigo());
				dtoProduto.setDescricao(produto.getDescricao());
				dtoProduto.setPreco(produto.getPreco());
				dtoProduto.setDataCadastro(produto.getDataCadastro());
				
				OptionalDouble optional = produto.getAvaliacoes().stream()
						.mapToDouble(avaliacao -> avaliacao.getRating().doubleValue())
						.average();
				
				if (optional.isPresent())				
					dtoProduto.setRating(optional.getAsDouble());
				else
					dtoProduto.setRating(0.0);
				
				return dtoProduto;
			}
		};
		
		return conversor;
	}
	
}
