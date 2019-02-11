package com.matera.trainning.bookstore.controller.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.matera.trainning.bookstore.controller.validation.impl.DescricaoAndPrecoValidator;

@Retention(RUNTIME)
@Target({TYPE, ANNOTATION_TYPE})
@Constraint(validatedBy = DescricaoAndPrecoValidator.class)
public @interface ValidaDescricaoAndPreco {
	
	String message() default "Campos inválidos";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
	
	String baseField();
	
	String matchField();
	
}
