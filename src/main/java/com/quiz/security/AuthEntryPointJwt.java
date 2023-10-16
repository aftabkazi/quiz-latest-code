package com.quiz.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.response.GlobalResponse;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint{

	private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		logger.error("Unauthorized error: {}", authException.getMessage());

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		
		/* To display customize message in front end */
		 Map<String, String> errorMessages = new HashMap<>();
			errorMessages.put("UnAuthorized"," Token has expired, Please login again" );
	        
			/* Object of global response */
	        GlobalResponse errorResponse = new GlobalResponse();
	        errorResponse.setId(null);
	        errorResponse.setMsg("Error occured due to following reasons");
	        errorResponse.setErrors(errorMessages);
	        errorResponse.setStatus(false);

		  final ObjectMapper mapper = new ObjectMapper();
		  mapper.writeValue(response.getOutputStream(), new GlobalResponse("Error occured due to following reasons",errorMessages,false));
	}
}	
