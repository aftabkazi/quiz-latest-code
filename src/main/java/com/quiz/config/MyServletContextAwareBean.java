package com.quiz.config;

import javax.servlet.ServletContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

@Component
public class MyServletContextAwareBean implements ServletContextAware {

	private ServletContext servletContext;
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	public void setAttribute(String key, Object value) {
		this.servletContext.setAttribute(key, value);
	}
	
	public Object getAttribute(String key) {
		return this.servletContext.getAttribute(key);
	}

	public void removeAttribute(String key) {
		this.servletContext.removeAttribute(key);
	}

}
