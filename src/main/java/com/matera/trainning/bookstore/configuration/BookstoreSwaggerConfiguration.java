package com.matera.trainning.bookstore.configuration;

import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class BookstoreSwaggerConfiguration {

	@Bean
	public Docket apiV1() {
		return new Docket(SWAGGER_2)
				.groupName("Versão 1.0")
	            .useDefaultResponseMessages(false)
				.select()
					.apis(basePackage("com.matera.trainning.bookstore.controller"))
					.paths(PathSelectors.ant("/v1/**"))
					.build()
				.apiInfo(metaDataV1());
	}
	
	private ApiInfo metaDataV1() {
		return new ApiInfoBuilder()
				.title("Livraria API")
				.description("Documentação de API REST com Swagger")
				.version("1.0")
				.license("Apache License Version 2.0")
				.licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
				.contact(new Contact("Douglas Ferreira de Borba", "douglasfborba@github.com", "douglas.borba@matera.com"))
				.build();
	}
	
	
	@Bean
	public Docket apiV2() {
		return new Docket(SWAGGER_2)
				.groupName("Versão 2.0")
	            .useDefaultResponseMessages(false)
				.select()
					.apis(basePackage("com.matera.trainning.bookstore.controller"))
					.paths(PathSelectors.ant("/v2/**"))
					.build()
				.apiInfo(metaDataV2());
	}

	private ApiInfo metaDataV2() {
		return new ApiInfoBuilder()
				.title("Livraria API")
				.description("Documentação de API REST com Swagger")
				.version("2.0")
				.license("Apache License Version 2.0")
				.licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
				.contact(new Contact("Douglas Ferreira de Borba", "douglasfborba@github.com", "douglas.borba@matera.com"))
				.build();
	}

}
