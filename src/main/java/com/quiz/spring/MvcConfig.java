package com.quiz.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//@EnableWebMvc

// this configuration is done to upload image in folder ...
// create a folder in local disk C and name the folder as uploads then all the uploaded images are stored in that location 

@Configuration
public class MvcConfig implements WebMvcConfigurer {
	
	@Value("${extern.resoures.path}")
	private String path;
	
	@Value("${extern.resources.Dir}")
	private String resourceDir;
	
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/"+resourceDir+"/**").addResourceLocations("file:"+path);
	}
}
