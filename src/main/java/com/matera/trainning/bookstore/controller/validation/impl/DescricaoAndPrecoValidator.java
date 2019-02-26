package com.matera.trainning.bookstore.controller.validation.impl;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import com.matera.trainning.bookstore.controller.validation.ValidaDescricaoAndPreco;

@Component
public class DescricaoAndPrecoValidator implements ConstraintValidator<ValidaDescricaoAndPreco, Object> {

	private static final int CONST_VALOR_CORTE = 10;
	
	private String baseField;
	private String matchField;

	@Override
	public void initialize(ValidaDescricaoAndPreco constraint) {
		baseField = constraint.baseField();
		matchField = constraint.matchField();
	}

	@Override
	public boolean isValid(Object valor, ConstraintValidatorContext contexto) {
		boolean resultado = true;
		
		try {
			final String descricao = (String) getCampoValor(valor, this.baseField);
	        final BigDecimal preco = new BigDecimal(getCampoValor(valor, this.matchField).toString());
			
	        if (descricao.length() > CONST_VALOR_CORTE)
	        	resultado = isPrecoMaiorQueValorDeCorte(preco);
				
		} catch (Exception ex) {
			resultado = false;
		}
		
		return resultado;
	}

	private Object getCampoValor(Object object, String nomeCampo) throws NoSuchFieldException, IllegalAccessException {       
        Field campo = object.getClass().getDeclaredField(nomeCampo);
        campo.setAccessible(true);        
        return campo.get(object);
    }

	private boolean isPrecoMaiorQueValorDeCorte(final BigDecimal preco) {
		return preco.doubleValue() > CONST_VALOR_CORTE;
	}
	
}
