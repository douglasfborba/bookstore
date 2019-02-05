package com.matera.trainning.bookstore.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.matera.trainning.bookstore.domain.HistoricoDePreco;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class HistoricoDePrecoDTO {
	
	@Getter @Setter
    @JsonView @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime dataHoraAlteracao;
	
	@Getter @Setter
	@JsonView
	private BigDecimal preco;
	
	@Getter @Setter 
	@JsonView
	private ProdutoDTO produto;	

	public static final Converter<HistoricoDePreco, HistoricoDePrecoDTO> getConverter() {
		Converter<HistoricoDePreco, HistoricoDePrecoDTO> conversor = new Converter<HistoricoDePreco, HistoricoDePrecoDTO>() {
			
			@Override
			public HistoricoDePrecoDTO convert(MappingContext<HistoricoDePreco, HistoricoDePrecoDTO> contexto) {
				HistoricoDePreco historico = contexto.getSource();
								
				HistoricoDePrecoDTO dtoHistorico = new HistoricoDePrecoDTO();
				ModelMapper modelMapper = new ModelMapper();
				
				dtoHistorico.setProduto(modelMapper.map(historico.getProduto(), ProdutoDTO.class));
				dtoHistorico.setPreco(historico.getPreco());
				dtoHistorico.setDataHoraAlteracao(historico.getDataHoraAlteracao());
				
				return dtoHistorico;
			}
		};
		
		return conversor;
	}
}
