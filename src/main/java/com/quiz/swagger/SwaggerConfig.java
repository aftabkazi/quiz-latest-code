package com.quiz.swagger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
	
	public static final String AUTHORIZATION_HEADER="Authorization";

	@Bean
	public Docket api() { 
		
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(getinfo())
				.securityContexts(securityContext())
				.securitySchemes(Arrays.asList(apiKey())) 
				.select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
	}
				
	private ApiInfo getinfo() { 
		return new ApiInfo("Quiz Web Application","This project is developed by CDAC Hyderabad", 
				"1.0","terms of service",new Contact("CDAC","gmail id",""),"License of APIs","API licence URL",Collections.emptyList());
	}
	
	/* API Key is used to provide a level of security and access control to APIS */
	private ApiKey apiKey() {
		return new ApiKey("JWT", "Authorization", "header");
	}

	private List<SecurityContext> securityContext() {
		return Arrays.asList(SecurityContext.builder().securityReferences(defaultAuth()).build());
	}
	
	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		return Arrays.asList(new SecurityReference("JWT", new AuthorizationScope[] {authorizationScope}));
	}
}
