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
		try {
			final String descricao = (String) getCampoValor(valor, this.baseField);
	        final BigDecimal preco = new BigDecimal(getCampoValor(valor, this.matchField).toString());
			return descricao.length() > CONST_VALOR_CORTE ? preco.doubleValue() > CONST_VALOR_CORTE : true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	private Object getCampoValor(Object object, String nomeCampo) throws Exception {       
        Field campo = object.getClass().getDeclaredField(nomeCampo);
        campo.setAccessible(true);        
        return campo.get(object);
    }

}
