package com.matera.trainning.bookstore.configuration;

import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class BookstoreSwaggerConfiguration {

	@Bean
	public Docket api() {
		return new Docket(SWAGGER_2)
	            .useDefaultResponseMessages(false)
				.select()
					.apis(basePackage("com.matera.trainning.bookstore.controller"))
					.build()
				.apiInfo(metaData());
	}

	private ApiInfo metaData() {
		return new ApiInfoBuilder()
				.title("Livraria API")
				.description("Documentação de API REST com Swagger")
				.version("1.0")
				.license("Apache License Version 2.0")
				.licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
				.contact(new Contact("Douglas Ferreira de Borba", "douglasfborba@github.com", "douglas.borba@matera.com"))
				.build();
	}

}
